package okodee.vom.domain.auth.exception;

import okodee.vom.global.exception.ErrorCode;
import okodee.vom.global.exception.VomException;

public class AuthException extends VomException {

    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }
}
