package okodee.vom.global.exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {
    private final Instant timestamp;
    private final String code;
    private final String message;
    private final String path;
    private final Map<String, Object> details;
    private final String exceptionType;
    private final int status;

    public ErrorResponse(VomException exception, int status, String path) {
        this(Instant.now(), exception.getErrorCode().name(), exception.getMessage(), path, exception.getDetails(), exception.getClass().getSimpleName(), status);
    }

    public ErrorResponse(Exception exception, int status, String path) {
        this(Instant.now(), exception.getClass().getSimpleName(), exception.getMessage(), path, new HashMap<>(), exception.getClass().getSimpleName(), status);
    }
}