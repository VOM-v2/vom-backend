package okodee.vom.domain.auth.exception;

import okodee.vom.domain.user.exception.UserException;
import okodee.vom.global.exception.ErrorCode;

public class DuplicateEmailException extends UserException {

    public DuplicateEmailException() { super(ErrorCode.DUPLICATE_EMAIL); }

    public static DuplicateEmailException withEmail(String email) {
        DuplicateEmailException exception = new DuplicateEmailException();
        exception.addDetail("email", email);
        return exception;
    }
}
