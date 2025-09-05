package next.career.domain.openai.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import next.career.domain.embedding.service.EmbeddingService;
import next.career.domain.job.controller.dto.GetRoadMapDto;
import next.career.domain.openai.dto.AiChatDto;
import next.career.domain.openai.dto.RecommendDto;
import next.career.domain.openai.entity.Prompt;
import next.career.domain.openai.repository.PromptRepository;
import next.career.domain.pinecone.service.PineconeService;
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

        // TODO: 유저 정보 가져와서 프롬프트에 넣기

        List<Prompt> occupation = promptRepository.findAllByTag("occupation");

        String system = occupation.stream()
                .map(Prompt::getContent)
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining("\n"));

        Map<String, Object> body = setPrompt(system);

        try {
            Map res = requestOpenAI(body);

            if (res == null) return RecommendDto.OccupationResponse.builder().occupationList(List.of()).build();

            List<Map<String, Object>> choices = (List<Map<String, Object>>) res.get("choices");
            if (choices == null || choices.isEmpty())
                return RecommendDto.OccupationResponse.builder().occupationList(List.of()).build();

            String content = getContent(choices);

            RecommendDto.OccupationResponse dto =
                    MAPPER.readValue(content, RecommendDto.OccupationResponse.class);

            List<String> list = Optional.ofNullable(dto.getOccupationList()).orElseGet(List::of)
                    .stream()
                    .filter(Objects::nonNull)
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .distinct()
                    .limit(3)
                    .toList();

            return RecommendDto.OccupationResponse.builder().occupationList(list).build();
        } catch (Exception e) {
            throw new CoreException(GlobalErrorType.GET_RECOMMEND_OCCUPATION_ERROR);
        }
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

        Map<String, Object> body = setPrompt(finalSystemPrompt);

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

    private static Map<String, Object> setPrompt(String system) {
        List<Map<String, Object>> messages = new ArrayList<>();

        if (!system.isBlank()) {
            messages.add(Map.of("role", "system", "content", system));
        }

        Map<String, Object> body = Map.of(
                "model", "gpt-4o",
                "messages", messages,
                "temperature", 0.2,
                "max_tokens", 800
        );
        return body;
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


    public RecommendDto.JobResponse getRecommendJob(Member member) {
        List<Float> vector = embeddingService.getEmbeddingMember(member).block();

        return pineconeService.getRecommendJob(vector);
    }


    public AiChatDto.OptionResponse getOptions(int sequence, MemberDetail memberDetail) {

        List<Prompt> aiChat = promptRepository.findAllByTag("ai 채팅 " + sequence);

        String system = aiChat.stream()
                .map(Prompt::getContent)
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining("\n"));

        String memberDetailText = convertService.convertMemberDetailToText(memberDetail);

        String finalSystemPrompt = system + "\n\n[사용자 정보]\n" + memberDetailText;

        Map<String, Object> body = setPrompt(finalSystemPrompt);

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
                    .limit(3)
                    .toList();

            return AiChatDto.OptionResponse.builder().optionList(list).build();
        } catch (Exception e) {
            throw new CoreException(GlobalErrorType.GET_AI_CHAT_OPTION_FAILED);
        }

    }










}
