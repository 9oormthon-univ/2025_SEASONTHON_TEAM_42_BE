package next.career.domain.UserJobMap.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import next.career.global.apiPayload.exception.ErrorType;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberJobMapErrorType implements ErrorType {
    BOOKMARK_INTERNAL_ERROR(HttpStatus.UNAUTHORIZED, "유효하지 않은 인증 코드입니다."),
    BOOKMARK_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 북마크입니다."),
    BOOKMARK_CANCEL_INTERNAL_ERROR(HttpStatus.UNAUTHORIZED, "북마크 삭제 중 에러가 발생하였습니다ㅏ."),
    BOOKMARK_REGISTER_INTERNAL_ERROR(HttpStatus.UNAUTHORIZED, "북마크 등록 중 에러가 발생하였습니다ㅏ."),

    ;

    private final HttpStatus status;

    private final String message;
}
