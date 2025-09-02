package next.career.domain.openai;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import next.career.domain.openai.dto.RecommendDto;
import next.career.domain.openai.entity.Prompt;
import next.career.domain.openai.repository.PromptRepository;
import next.career.global.apiPayload.exception.CoreException;
import next.career.global.apiPayload.exception.GlobalErrorType;
import org.springframework.stereotype.Service;
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

    public RecommendDto.OccupationResponse getRecommendOccupation(Long userId) {

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

    public RecommendDto.RoadMapResponse getRecommendRoadMap(Long userId) {
        List<Prompt> roadmap = promptRepository.findAllByTag("roadmap");

        String system = roadmap.stream()
                .map(Prompt::getContent)
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining("\n"));

        Map<String, Object> body = setPrompt(system);

        try {
            Map res = requestOpenAI(body);

            if (res == null) return RecommendDto.RoadMapResponse.builder().roadMapList(List.of()).build();

            List<Map<String, Object>> choices = (List<Map<String, Object>>) res.get("choices");
            if (choices == null || choices.isEmpty())
                return RecommendDto.RoadMapResponse.builder().roadMapList(List.of()).build();

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

            return RecommendDto.RoadMapResponse.builder().roadMapList(list).build();
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


}
