package okodee.vom.global.exception;

public class UnauthorizedAccessException extends VomException {

    public UnauthorizedAccessException() {
        super(ErrorCode.UNAUTHORIZED_ACCESS);
    }
}
