package next.career.domain.education.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import next.career.global.apiPayload.exception.ErrorType;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum EducationErrorType implements ErrorType {
    EDUCATION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 교육이 존재하지 않습니다."),
    ;

    private final HttpStatus status;

    private final String message;
}
