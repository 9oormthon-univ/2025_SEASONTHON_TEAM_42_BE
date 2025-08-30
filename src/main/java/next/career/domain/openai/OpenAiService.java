package next.career.domain.openai;

import lombok.RequiredArgsConstructor;
import next.career.domain.openai.entity.Prompt;
import next.career.domain.openai.repository.PromptRepository;
import next.career.global.apiPayload.exception.CoreException;
import next.career.global.apiPayload.exception.GlobalErrorType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OpenAiService {

    private final WebClient openAiClient;
    private final PromptRepository promptRepository;

    public String getRecommendOccupation() {

        List<Prompt> occupation = promptRepository.findAllByTag("occupation");

        List<Map<String, Object>> messages = new ArrayList<>();

        String system = occupation.stream()
                .map(Prompt::getContent)
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining("\n"));

        if (!system.isBlank()) {
            messages.add(Map.of("role", "system", "content", system));
        }

        Map<String, Object> body = Map.of(
                "model", "gpt-4o",
                "messages", messages,
                "temperature", 0.2,
                "max_tokens", 800
        );

        try {
            Map res = openAiClient.post()
                    .uri("/chat/completions")
                    .bodyValue(body)
                    .retrieve()
                    .onStatus(s -> s.is4xxClientError() || s.is5xxServerError(),
                            r -> r.bodyToMono(String.class)
                                    .flatMap(msg -> Mono.error(new RuntimeException("OpenAI error: " + msg))))
                    .bodyToMono(Map.class)
                    .block(java.time.Duration.ofSeconds(30));

            if (res == null) return "";
            List<Map<String, Object>> choices = (List<Map<String, Object>>) res.get("choices");
            if (choices == null || choices.isEmpty()) return "";
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            return (String) message.get("content");
        } catch (Exception e) {
            throw new CoreException(GlobalErrorType.GET_RECOMMEND_OCCUPATION_ERROR);
        }

    }
}
