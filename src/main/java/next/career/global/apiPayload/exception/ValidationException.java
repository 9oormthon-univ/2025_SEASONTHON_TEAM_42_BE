package next.career.global.apiPayload.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import java.util.List;

@Getter
public class ValidationException implements ErrorType {
    private final HttpStatus status = HttpStatus.BAD_REQUEST;
    private final List<String> errors;

    public ValidationException(List<String> errors) {
        this.errors = errors;
    }

    @Override
    public String getMessage() {
        return "Validation failed: " + String.join(", ", errors);
    }

    @Override
    public String name() {
        return "VALIDATION_ERROR";
    }
}
