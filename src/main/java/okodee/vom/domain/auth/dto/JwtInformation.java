package okodee.vom.domain.auth.dto;

import okodee.vom.domain.user.dto.UserDto;

public record JwtInformation(
    UserDto userDto,
    String accessToken,
    String refreshToken
) {

}
