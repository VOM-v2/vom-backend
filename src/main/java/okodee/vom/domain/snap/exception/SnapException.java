package okodee.vom.domain.snap.exception;

import okodee.vom.global.exception.ErrorCode;
import okodee.vom.global.exception.VomException;

public class SnapException extends VomException {

  public SnapException(ErrorCode errorCode) {
    super(errorCode);
  }
}
