package next.career.domain.UserEducationMap.exception;

import next.career.global.apiPayload.exception.CoreException;
import next.career.global.apiPayload.exception.ErrorType;

public class MemberEducationMapException extends CoreException {

    public MemberEducationMapException(ErrorType errorType) {
        super(errorType);
    }
}
