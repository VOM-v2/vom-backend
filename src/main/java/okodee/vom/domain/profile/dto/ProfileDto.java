package okodee.vom.domain.profile.dto;

import java.time.LocalDate;
import java.util.UUID;
import okodee.vom.domain.user.entity.Gender;

public record ProfileDto(
    UUID userId,
    String name,
    Gender gender,
    LocalDate birthDate,
    String profileImageUrl
) {

}
