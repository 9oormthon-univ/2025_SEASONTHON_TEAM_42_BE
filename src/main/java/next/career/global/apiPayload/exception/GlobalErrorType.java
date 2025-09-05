package next.career.global.apiPayload.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GlobalErrorType implements ErrorType {

    // Member
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "토큰이 존재하지 않습니다."),
    TOKEN_INVALID(HttpStatus.BAD_REQUEST, "토큰이 유효하지 않습니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 멤버입니다."),
    MEMBER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 멤버입니다."),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

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
    GET_AI_CHAT_OPTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "AI 선택형 보기 생성 실패"),
    MEMBER_DETAIL_SEQUENCE_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "AI 채팅에 해당 순서의 질문이 없습니다"),

    // RoadMap
    ROAD_MAP_ACTION_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "로드맵 할 일이 존재하지 않습니다"),
    ROAD_MAP_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "로드이 존재하지 않습니다"),

    ;

    private final HttpStatus status;

    private final String message;
}