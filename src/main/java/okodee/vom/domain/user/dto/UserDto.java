package okodee.vom.domain.user.dto;

import java.time.Instant;
import java.util.UUID;
import okodee.vom.domain.user.entity.Role;

public record UserDto(
    UUID id,
    Instant createdAt,
    String email,
    String name,
//    String nickname,
    Role role,
    Boolean locked
) {

}
