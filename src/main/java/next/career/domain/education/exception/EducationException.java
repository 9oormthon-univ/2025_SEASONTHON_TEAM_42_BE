package next.career.domain.education.exception;

import next.career.global.apiPayload.exception.CoreException;
import next.career.global.apiPayload.exception.ErrorType;

public class EducationException extends CoreException {

    public EducationException(ErrorType errorType) {
        super(errorType);
    }
}
