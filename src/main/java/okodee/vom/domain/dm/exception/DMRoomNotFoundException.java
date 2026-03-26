package okodee.vom.domain.dm.exception;

import okodee.vom.global.exception.ErrorCode;

public class DMRoomNotFoundException extends DMException {
    public DMRoomNotFoundException() {
        super(ErrorCode.DM_ROOM_NOT_FOUND);
    }
}
