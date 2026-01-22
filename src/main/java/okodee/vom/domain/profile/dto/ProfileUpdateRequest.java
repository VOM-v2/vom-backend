package okodee.vom.domain.profile.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import okodee.vom.domain.user.entity.Gender;

public record ProfileUpdateRequest(
    @NotBlank(message = "사용자 이름은 필수입니다")
    @Size(min = 2, max = 20, message = "사용자 이름은 2자 이상 20자 이하여야 합니다")
    String name,

    @NotNull(message = "성별은 필수입니다.")
    Gender gender,

    @NotNull(message = "생년월일은 필수입니다.")
    @PastOrPresent(message = "생년월일은 현재 또는 과거여야 합니다.")
    LocalDate birthDate
) {

}
