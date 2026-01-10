package okodee.vom.domain.user.exception;

import okodee.vom.global.exception.ErrorCode;
import okodee.vom.global.exception.VomException;

public class UserException extends VomException {

    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
