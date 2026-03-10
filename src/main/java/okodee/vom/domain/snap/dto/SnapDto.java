package okodee.vom.domain.snap.dto;

import java.time.Instant;
import java.util.UUID;

public record SnapDto(
    UUID id,
    Instant createdAt,
    UUID userId,
    String nickname,
    String content,
    String snapImageUrl
) {

}
