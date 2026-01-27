package okodee.vom.domain.profile.exception;

import okodee.vom.global.exception.ErrorCode;
import okodee.vom.global.exception.VomException;

public class KeywordException extends VomException {

    public KeywordException(ErrorCode errorCode) {
      super(errorCode);
    }
}
