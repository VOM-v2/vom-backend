package okodee.vom.domain.dm.dto;

import java.util.UUID;

public record DirectMessageRoomResponse(
    UUID roomId,
    UUID receiverId,
    String receiverNickname
) {

}
