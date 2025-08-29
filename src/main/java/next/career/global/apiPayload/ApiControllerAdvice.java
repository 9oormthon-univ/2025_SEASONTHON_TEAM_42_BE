package next.career.global.apiPayload;

import lombok.extern.log4j.Log4j2;
import next.career.global.apiPayload.exception.CoreException;
import next.career.global.apiPayload.exception.GlobalErrorType;
import next.career.global.apiPayload.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Log4j2
public class ApiControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception e) {
        log.error("Exception : {}", e.getMessage(), e);
        return new ResponseEntity<>(ApiResponse.error(GlobalErrorType.INTERNAL_SERVER_ERROR), GlobalErrorType.INTERNAL_SERVER_ERROR.getStatus());
    }

    @ExceptionHandler(CoreException.class)
    public ResponseEntity<ApiResponse<?>> handleCoreException(CoreException e) {
        log.error("CoreException : {}", e.getMessage(), e);
        return new ResponseEntity<>(ApiResponse.error(e.getErrorType()), e.getErrorType().getStatus());
    }

}