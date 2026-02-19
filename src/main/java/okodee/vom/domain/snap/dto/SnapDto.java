package okodee.vom.domain.snap.dto;

import java.time.Instant;
import java.util.UUID;
import okodee.vom.domain.user.dto.UserDto;

public record SnapDto(
    UUID id,
    Instant createdAt,
    UserDto user,
    String content
) {

}
