package okodee.vom.domain.dm.exception;

import okodee.vom.global.exception.ErrorCode;

public class DMUnauthorizedException extends DMException {
    public DMUnauthorizedException() {
        super(ErrorCode.DM_UNAUTHORIZED);
    }
}
