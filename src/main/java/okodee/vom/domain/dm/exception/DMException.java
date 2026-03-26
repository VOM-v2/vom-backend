package okodee.vom.domain.dm.exception;

import okodee.vom.global.exception.ErrorCode;
import okodee.vom.global.exception.VomException;

public class DMException extends VomException {

  public DMException(ErrorCode errorCode) {
    super(errorCode);
  }
}
