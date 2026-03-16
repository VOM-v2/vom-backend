package okodee.vom.domain.dm.dto;

import java.util.UUID;

public record DirectMessageRoomListResponse(
    UUID roomId,
    UUID partnerId,
    String partnerNickname
) {

}
