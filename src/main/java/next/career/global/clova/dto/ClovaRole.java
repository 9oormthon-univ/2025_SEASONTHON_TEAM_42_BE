package next.career.global.clova.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ClovaRole {
    SYSTEM("system"),
    USER("user"),
    ASSISTANT("assistant"),
    ;

    private final String value;

}
