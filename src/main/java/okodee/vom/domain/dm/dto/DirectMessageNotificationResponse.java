package okodee.vom.domain.dm.dto;

import java.util.UUID;

public record DirectMessageNotificationResponse(
    UUID roomId,
    UUID senderId,
    String senderNickname,
    String contentPreview
) {

}
