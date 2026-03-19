package okodee.vom.domain.dm.exception;

import okodee.vom.global.exception.ErrorCode;

public class DMSelfChatNotAllowedException extends DMException {

    public DMSelfChatNotAllowedException() {
        super(ErrorCode.DM_SELF_CHAT_NOT_ALLOWED);
    }
}
