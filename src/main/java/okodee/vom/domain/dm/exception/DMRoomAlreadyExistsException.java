package okodee.vom.domain.dm.exception;

import okodee.vom.global.exception.ErrorCode;

public class DMRoomAlreadyExistsException extends DMException {
    public DMRoomAlreadyExistsException() {
        super(ErrorCode.DM_ROOM_ALREADY_EXISTS);
    }
}
