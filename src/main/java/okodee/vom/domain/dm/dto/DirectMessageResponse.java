package okodee.vom.domain.dm.dto;

import java.time.Instant;
import java.util.UUID;

public record DirectMessageResponse(
    UUID messageId,
    UUID senderId,
    String content,
    boolean isRead,
    Instant createdAt
) {

}
