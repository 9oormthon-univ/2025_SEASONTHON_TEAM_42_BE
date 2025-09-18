package next.career.domain.openai.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import next.career.domain.embedding.service.EmbeddingService;
import next.career.domain.job.controller.dto.GetRoadMapDto;
import next.career.domain.job.service.dto.PineconeRecommendDto;
import next.career.domain.openai.dto.AiChatDto;
import next.career.domain.openai.dto.RecommendDto;
import next.career.domain.openai.entity.Prompt;
import next.career.domain.openai.repository.PromptRepository;
import next.career.domain.pinecone.service.PineconeService;
import next.career.domain.report.controller.dto.GetStrengthReportDto;
import next.career.domain.roadmap.controller.dto.RoadmapDto;
import next.career.domain.user.entity.Member;
import next.career.domain.user.entity.MemberDetail;
import next.career.global.apiPayload.exception.CoreException;
import next.career.global.apiPayload.exception.GlobalErrorType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OpenAiService {

    private final WebClient openAiClient;
    private final PromptRepository promptRepository;
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private final EmbeddingService embeddingService;
    private final PineconeService pineconeService;
    private final ConvertService convertService;

    public RecommendDto.OccupationResponse getRecommendOccupation(Member member) {

        List<Prompt> occupation = promptRepository.findAllByTag("occupation");

        String system = occupation.stream()
                .map(Prompt::getContent)
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining("\n"));

        MemberDetail memberDetail = member.getMemberDetail();

        String memberDetailText = convertService.convertMemberDetailToText(memberDetail);

        String finalSystemPrompt = system + "\n\n[사용자 정보]\n" + memberDetailText;

        Map<String, Object> body = setRecommendOccupationPrompt(finalSystemPrompt);

        try {
            Map res = requestOpenAI(body);

            if (res == null) {
                return RecommendDto.OccupationResponse.builder()
                        .occupationList(List.of())
                        .build();
            }

            List<Map<String, Object>> choices = (List<Map<String, Object>>) res.get("choices");
            if (choices == null || choices.isEmpty()) {
                return RecommendDto.OccupationResponse.builder()
                        .occupationList(List.of())
                        .build();
            }

            String content = getContent(choices);

            RecommendDto.OccupationResponse dto =
                    MAPPER.readValue(content, RecommendDto.OccupationResponse.class);

            List<RecommendDto.OccupationResponse.Occupation> list =
                    Optional.ofNullable(dto.getOccupationList()).orElseGet(List::of)
                            .stream()
                            .filter(Objects::nonNull)
                            .distinct()
                            .limit(3)
                            .toList();

            return RecommendDto.OccupationResponse.builder()
                    .occupationList(list)
                    .build();

        } catch (Exception e) {
            throw new CoreException(GlobalErrorType.GET_RECOMMEND_OCCUPATION_ERROR);
        }

    }

    private static Map<String, Object> setRecommendOccupationPrompt(String system) {
        List<Map<String, Object>> messages = new ArrayList<>();

        if (!system.isBlank()) {
            messages.add(Map.of("role", "system", "content", system));
        }

        Map<String, Object> responseFormat = Map.of(
                "type", "json_schema",
                "json_schema", Map.of(
                        "name", "occupation_list_response",
                        "schema", Map.of(
                                "type", "object",
                                "properties", Map.of(
                                        "occupationList", Map.of(
                                                "type", "array",
                                                "items", Map.of(
                                                        "type", "object",
                                                        "properties", Map.of(
                                                                "imageUrl", Map.of("type", "string"),
                                                                "occupationName", Map.of("type", "string"),
                                                                "description", Map.of("type", "string"),
                                                                "strength", Map.of("type", "string"),
                                                                "workCondition", Map.of("type", "string"),
                                                                "wish", Map.of("type", "string"),
                                                                "score", Map.of(
                                                                        "type", "string",
                                                                        "pattern", "^(100|[0-9]{1,2})$" // 0~100 문자열
                                                                )
                                                        ),
                                                        "required", List.of(
                                                                "imageUrl",
                                                                "occupationName",
                                                                "description",
                                                                "strength",
                                                                "workCondition",
                                                                "wish",
                                                                "score"
                                                        )
                                                )
                                        )
                                ),
                                "required", List.of("occupationList")
                        )
                )
        );

        return Map.of(
                "model", "gpt-4o",
                "messages", messages,
                "temperature", 0.2,
                "max_tokens", 800,
                "response_format", responseFormat
        );
    }


    public RecommendDto.RoadMapResponse getRecommendRoadMap(GetRoadMapDto.Request roadmapRequest, Member member) {
        List<Prompt> roadmap = promptRepository.findAllByTag("roadmap");

        String system = roadmap.stream()
                .map(Prompt::getContent)
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining("\n"));

        String roadmapRequestText = convertService.convertRoadmapRequestToText(roadmapRequest);

        String finalSystemPrompt = system + "\n\n[사용자 요구사항]\n" + roadmapRequestText;

        Map<String, Object> body = setRecommendRoadmapPrompt(finalSystemPrompt);

        try {
            Map res = requestOpenAI(body);

            if (res == null) return RecommendDto.RoadMapResponse.builder().steps(List.of()).build();

            List<Map<String, Object>> choices = (List<Map<String, Object>>) res.get("choices");
            if (choices == null || choices.isEmpty())
                return RecommendDto.RoadMapResponse.builder().steps(List.of()).build();

            String content = getContent(choices);

            RecommendDto.RoadMapResponse dto =
                    MAPPER.readValue(content, RecommendDto.RoadMapResponse.class);

            List<RecommendDto.RoadMapResponse.RoadMapStep> steps =
                    Optional.ofNullable(dto.getSteps()).orElseGet(List::of)
                            .stream()
                            .filter(Objects::nonNull)
                            .toList();

            return RecommendDto.RoadMapResponse.builder().steps(steps).build();

        } catch (Exception e) {
            throw new CoreException(GlobalErrorType.GEt_RECOMMEND_ROADMAP_ERROR);
        }
    }

    private static Map<String, Object> setRecommendRoadmapPrompt(String system) {
        List<Map<String, Object>> messages = new ArrayList<>();

        if (!system.isBlank()) {
            messages.add(Map.of("role", "system", "content", system));
        }

        Map<String, Object> responseFormat = Map.of(
                "type", "json_schema",
                "json_schema", Map.of(
                        "name", "steps_response",
                        "schema", Map.of(
                                "type", "object",
                                "properties", Map.of(
                                        "steps", Map.of(
                                                "type", "array",
                                                "items", Map.of(
                                                        "type", "object",
                                                        "properties", Map.of(
                                                                "period", Map.of("type", "string"),
                                                                "category", Map.of("type", "string"),
                                                                "isCompleted", Map.of("type", "boolean"),
                                                                "actions", Map.of(
                                                                        "type", "array",
                                                                        "items", Map.of(
                                                                                "type", "object",
                                                                                "properties", Map.of(
                                                                                        "action", Map.of("type", "string"),
                                                                                        "isCompleted", Map.of("type", "boolean")
                                                                                ),
                                                                                "required", List.of("action", "isCompleted")
                                                                        )
                                                                )
                                                        ),
                                                        "required", List.of("period", "category", "isCompleted", "actions")
                                                )
                                        )
                                ),
                                "required", List.of("steps")
                        )
                )
        );

        return Map.of(
                "model", "gpt-4o",
                "messages", messages,
                "temperature", 0.2,
                "max_tokens", 800,
                "response_format", responseFormat
        );
    }


    private static String getContent(List<Map<String, Object>> choices) {
        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
        String content = (String) message.get("content");
        if (content == null) content = "";

        content = stripCodeFence(content);
        return content;
    }

    private Map requestOpenAI(Map<String, Object> body) {

        Map res = openAiClient.post()
                .uri("/chat/completions")
                .bodyValue(body)
                .retrieve()
                .onStatus(s -> s.is4xxClientError() || s.is5xxServerError(),
                        r -> r.bodyToMono(String.class)
                                .flatMap(msg -> Mono.error(new RuntimeException("OpenAI error: " + msg))))
                .bodyToMono(Map.class)
                .block(java.time.Duration.ofSeconds(30));

        log.info("request open ai response = {}", res);
        return res;
    }

    private static Map<String, Object> setAIChatOptionPrompt(String system) {
        List<Map<String, Object>> messages = new ArrayList<>();

        if (!system.isBlank()) {
            messages.add(Map.of("role", "system", "content", system));
        }

        // response_format 정의
        Map<String, Object> responseFormat = Map.of(
                "type", "json_schema",
                "json_schema", Map.of(
                        "name", "option_list_response",
                        "schema", Map.of(
                                "type", "object",
                                "properties", Map.of(
                                        "optionList", Map.of(
                                                "type", "array",
                                                "items", Map.of("type", "string")
                                        )
                                ),
                                "required", List.of("optionList")
                        )
                )
        );

        // 최종 body
        return Map.of(
                "model", "gpt-4o",
                "messages", messages,
                "temperature", 0.2,
                "max_tokens", 800,
                "response_format", responseFormat
        );
    }


    private static String stripCodeFence(String s) {
        String t = s.trim();
        if (t.startsWith("```")) {
            t = t.replaceFirst("^```[a-zA-Z]*\\s*", "");
            int idx = t.lastIndexOf("```");
            if (idx >= 0) t = t.substring(0, idx);
        }
        return t.trim();
    }


    public List<PineconeRecommendDto> getRecommendJob(Member member) {
        List<Float> vector = embeddingService.getEmbeddingMember(member).block();

        return pineconeService.getRecommendJob(vector);
    }

    public List<PineconeRecommendDto> getRecommendEducation(Member member) {
        List<Float> vector = embeddingService.getEmbeddingMember(member).block();

        return pineconeService.getRecommendEducation(vector);
    }


    public AiChatDto.OptionResponse getAIChatOptions(int sequence, MemberDetail memberDetail) {

        List<Prompt> aiChat = promptRepository.findAllByTag("ai 채팅 " + sequence);

        String system = aiChat.stream()
                .map(Prompt::getContent)
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining("\n"));

        String memberDetailText = convertService.convertMemberDetailToText(memberDetail);

        String finalSystemPrompt = system + "\n\n[사용자 정보]\n" + memberDetailText;

        Map<String, Object> body = setAIChatOptionPrompt(finalSystemPrompt);

        try {

            Map res = requestOpenAI(body);

            if (res == null) return AiChatDto.OptionResponse.builder().optionList(List.of()).build();

            List<Map<String, Object>> choices = (List<Map<String, Object>>) res.get("choices");
            if (choices == null || choices.isEmpty())
                return AiChatDto.OptionResponse.builder().optionList(List.of()).build();

            String content = getContent(choices);

            AiChatDto.OptionResponse dto =
                    MAPPER.readValue(content, AiChatDto.OptionResponse.class);

            List<String> list = Optional.ofNullable(dto.getOptionList()).orElseGet(List::of)
                    .stream()
                    .filter(Objects::nonNull)
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .distinct()
                    .limit(6)
                    .toList();

            return AiChatDto.OptionResponse.builder().optionList(list).build();
        } catch (Exception e) {
            throw new CoreException(GlobalErrorType.GET_AI_CHAT_OPTION_FAILED);
        }

    }

    public List<String> getRecommendRoadmapActionAI(String category) {

        List<Prompt> roadmapAction = promptRepository.findAllByTag("roadmap action");

        String system = roadmapAction.stream()
                .map(Prompt::getContent)
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining("\n"));

        String finalSystemPrompt = system + "\n\n[카테고리]\n" + category;

        Map<String, Object> body = setRecommendRoadmapActionPrompt(finalSystemPrompt);

        try {
            Map res = requestOpenAI(body);
            log.info("res = {}", res);

            if (res == null) return List.of();

            List<Map<String, Object>> choices = (List<Map<String, Object>>) res.get("choices");
            if (choices == null || choices.isEmpty()) return List.of();

            String content = getContent(choices);

            RoadmapDto.RoadmapActionRecommendResponse dto =
                    MAPPER.readValue(content, RoadmapDto.RoadmapActionRecommendResponse.class);

            return Optional.ofNullable(dto.getRecommendRoadmapActionList()).orElseGet(List::of)
                    .stream()
                    .filter(Objects::nonNull)
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .distinct()
                    .limit(4)
                    .toList();

        } catch (Exception e) {
            throw new CoreException(GlobalErrorType.GET_RECOMMEND_ROADMAP_ACTION_ERROR);
        }


    }

    private static Map<String, Object> setRecommendRoadmapActionPrompt(String system) {
        List<Map<String, Object>> messages = new ArrayList<>();

        if (!system.isBlank()) {
            messages.add(Map.of("role", "system", "content", system));
        }

        Map<String, Object> responseFormat = Map.of(
                "type", "json_schema",
                "json_schema", Map.of(
                        "name", "roadmap_action_recommend_response",
                        "schema", Map.of(
                                "type", "object",
                                "properties", Map.of(
                                        "recommendRoadmapActionList", Map.of(
                                                "type", "array",
                                                "items", Map.of("type", "string")
                                        )
                                ),
                                "required", List.of("recommendRoadmapActionList")
                        )
                )
        );


        return Map.of(
                "model", "gpt-4o",
                "messages", messages,
                "temperature", 0.5,
                "max_tokens", 800,
                "response_format", responseFormat
        );
    }

    public GetStrengthReportDto.Response createStrengthReport(Member member) {

        List<Prompt> roadmapAction = promptRepository.findAllByTag("strength report");

        String system = roadmapAction.stream()
                .map(Prompt::getContent)
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining("\n"));

        String finalSystemPrompt = system + "\n\n[사용자 정보]\n" + member.getMemberDetail();
        log.info("member detail = {}", member.getMemberDetail());

        Map<String, Object> body = setStrengthReportPrompt(finalSystemPrompt);

        try {
            Map res = requestOpenAI(body);
            log.info("res = {}", res);

            List<Map<String, Object>> choices = (List<Map<String, Object>>) res.get("choices");

            String content = getContent(choices);

            GetStrengthReportDto.Response dto =
                    MAPPER.readValue(content, GetStrengthReportDto.Response.class);

            return dto;

        } catch (Exception e) {
            throw new CoreException(GlobalErrorType.GET_STRENGTH_REPORT_ERROR);
        }

    }

    private Map<String, Object> setStrengthReportPrompt(String system) {

        List<Map<String, Object>> messages = new ArrayList<>();

        if (!system.isBlank()) {
            messages.add(Map.of("role", "system", "content", system));
        }

        Map<String, Object> responseFormat = Map.of(
                "type", "json_schema",
                "json_schema", Map.of(
                        "name", "response",
                        "schema", Map.of(
                                "type", "object",
                                "properties", Map.of(
                                        "reportList", Map.of(
                                                "type", "array",
                                                "items", Map.of(
                                                        "type", "object",
                                                        "properties", Map.of(
                                                                "strength", Map.of("type", "string"),
                                                                "experience", Map.of("type", "string"),
                                                                "keyword", Map.of(
                                                                        "type", "array",
                                                                        "items", Map.of("type", "string")
                                                                ),
                                                                "job", Map.of(
                                                                        "type", "array",
                                                                        "items", Map.of("type", "string")
                                                                )
                                                        ),
                                                        "required", List.of("strength", "experience", "keyword", "job")
                                                )
                                        )
                                ),
                                "required", List.of("reportList")
                        )
                )
        );

        return Map.of(
                "model", "gpt-4o",
                "messages", messages,
                "temperature", 0.5,
                "max_tokens", 800,
                "response_format", responseFormat
        );
    }
}
