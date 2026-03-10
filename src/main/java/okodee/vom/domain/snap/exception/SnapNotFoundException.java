package okodee.vom.domain.snap.exception;

import java.util.UUID;
import okodee.vom.global.exception.ErrorCode;

public class SnapNotFoundException extends SnapException {

    public SnapNotFoundException() { super(ErrorCode.SNAP_NOT_FOUND); }

    public static SnapNotFoundException withId(UUID snapId) {
        SnapNotFoundException exception = new SnapNotFoundException();
        exception.addDetail("snapId", snapId);
        return exception;
    }
}
