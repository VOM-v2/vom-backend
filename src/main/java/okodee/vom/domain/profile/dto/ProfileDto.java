package okodee.vom.domain.profile.dto;

import java.time.LocalDate;
import java.util.UUID;

public record ProfileDto(
    UUID userId,
    String name,
    String gender,
    LocalDate birthDate,
    String profileImageUrl
) {

}
