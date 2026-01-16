package okodee.vom.domain.auth.service;

import okodee.vom.domain.auth.dto.JwtInformation;
import okodee.vom.domain.auth.dto.SignupRequest;
import okodee.vom.domain.user.dto.UserDto;

public interface AuthService {
    UserDto signup(SignupRequest signupRequest);
    JwtInformation refreshToken(String refreshToken);
}
