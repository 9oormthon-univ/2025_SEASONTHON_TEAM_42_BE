package next.career.global.clova.dto.response;

import next.career.global.clova.dto.ClovaMessage;

public record ClovaResult(
        ClovaMessage message,
        String stopReason,
        int inputLength,
        int outputLength
) {
}

