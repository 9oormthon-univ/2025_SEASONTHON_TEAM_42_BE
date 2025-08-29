package next.career.global.clova.dto.request;

import next.career.global.clova.dto.ClovaMessage;

import java.util.List;

public record ClovaRequest(
        List<ClovaMessage> messages,
        int maxTokens
) {
    public static ClovaRequest from(List<ClovaMessage> messages) {
        return new ClovaRequest(messages, 1024);
    }
}