package okodee.vom.domain.dm.dto;

import java.util.UUID;

public record DirectMessageSendResult(
    DirectMessageResponse response,
    DirectMessageNotificationResponse notification,
    UUID receiverId,
    String receiverEmail
) {

}
