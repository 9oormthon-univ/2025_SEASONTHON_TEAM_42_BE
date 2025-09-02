package next.career.global.apiPayload.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GlobalErrorType implements ErrorType {

    //Redis
    REDIS_SET_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Redis에 값을 저장하는 데 실패했습니다."),
    REDIS_GET_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Redis에서 값을 가져오는 데 실패했습니다."),
    REDIS_DELETE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Redis에서 값을 삭제하는 데 실패했습니다."),
    SHA256_GENERATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SHA256 해시 생성에 실패했습니다."),

    // Common
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 내부 오류입니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근이 금지되었습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다."),
    JSON_PROCESSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "JSON 처리 중 오류가 발생했습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),

    // Job
    JOB_NOT_FOUND_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "해당하는 일자리를 찾을 수 없습니다"),

    // Open AI
    GET_RECOMMEND_OCCUPATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "직업 추천에 실패했습니다"),
    GEt_RECOMMEND_ROADMAP_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "로드맵 추천에 실패했습니다"),

    ;

    private final HttpStatus status;

    private final String message;
}