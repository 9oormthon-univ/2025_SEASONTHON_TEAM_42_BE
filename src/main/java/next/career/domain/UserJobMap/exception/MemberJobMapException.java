package next.career.domain.UserJobMap.exception;

import next.career.global.apiPayload.exception.CoreException;
import next.career.global.apiPayload.exception.ErrorType;

public class MemberJobMapException extends CoreException {

    public MemberJobMapException(ErrorType errorType) {
        super(errorType);
    }
}
