package okodee.vom.global.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(VomException.class)
    public ResponseEntity<ErrorResponse> handleVomException(VomException e, HttpServletRequest request) {
        log.error("커스텀 예외 발생: code={}, message={}", e.getErrorCode(), e.getMessage(), e);
        HttpStatus status = determineHttpStatus(e);
        ErrorResponse response = new ErrorResponse(e, status.value(), request.getRequestURI());
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException e,
        HttpServletRequest request) {
        log.error("요청 유효성 검사 실패: {}", e.getMessage());

        Map<String, Object> validationErrors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            validationErrors.put(fieldName, errorMessage);
        });

        ErrorResponse response = new ErrorResponse(
            Instant.now(),
            "VALIDATION_ERROR",
            "요청 데이터 유효성 검사에 실패했습니다",
            request.getRequestURI(),
            validationErrors,
            e.getClass().getSimpleName(),
            HttpStatus.BAD_REQUEST.value()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceeded(
        MaxUploadSizeExceededException e,
        HttpServletRequest request) {
        log.warn("파일 크기 초과: {}", e.getMessage());
        return ResponseEntity
            .status(HttpStatus.PAYLOAD_TOO_LARGE)  // 413 상태코드
            .body(new ErrorResponse(e, HttpStatus.PAYLOAD_TOO_LARGE.value(), request.getRequestURI()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e, HttpServletRequest request) {
        log.error("예상치 못한 오류 발생: {}", e.getMessage(), e);
        ErrorResponse response = new ErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR.value(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    private HttpStatus determineHttpStatus(VomException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        return switch (errorCode) {
            // Auth
            case DUPLICATE_EMAIL -> HttpStatus.CONFLICT;
            case INVALID_REQUEST -> HttpStatus.BAD_REQUEST;
            case INVALID_TOKEN, INVALID_USER_DETAILS -> HttpStatus.UNAUTHORIZED;

            // User
            case USER_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case UNAUTHORIZED_ACCESS -> HttpStatus.FORBIDDEN;

            // Keyword (추가)
            case KEYWORD_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case MAX_KEYWORDS_EXCEEDED -> HttpStatus.BAD_REQUEST;
            case DUPLICATE_KEYWORD -> HttpStatus.CONFLICT;

            // Snap
            case SNAP_NOT_FOUND -> HttpStatus.NOT_FOUND;

            // DM
            case DM_ROOM_ALREADY_EXISTS -> HttpStatus.CONFLICT;
            case DM_ROOM_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case DM_UNAUTHORIZED -> HttpStatus.FORBIDDEN;
            case DM_SELF_CHAT_NOT_ALLOWED -> HttpStatus.BAD_REQUEST;

            // Server
            case INTERNAL_SERVER_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}
