package okodee.vom.domain.auth.dto;

import okodee.vom.domain.user.dto.UserDto;

public record JwtDto(
    UserDto userDto,
    String accessToken
) {

}