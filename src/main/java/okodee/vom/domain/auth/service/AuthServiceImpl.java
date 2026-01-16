package okodee.vom.domain.auth.service;

import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okodee.vom.domain.auth.dto.JwtInformation;
import okodee.vom.domain.auth.dto.SignupRequest;
import okodee.vom.domain.user.dto.UserDto;
import okodee.vom.domain.user.entity.User;
import okodee.vom.domain.auth.exception.DuplicateEmailException;
import okodee.vom.domain.user.mapper.UserMapper;
import okodee.vom.domain.user.repository.UserRepository;
import okodee.vom.global.exception.ErrorCode;
import okodee.vom.global.exception.VomException;
import okodee.vom.global.security.JwtTokenProvider;
import okodee.vom.global.security.VomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;

    @Transactional
    @Override
    public UserDto signup(SignupRequest signupRequest) {
        log.debug("사용자 생성 시작: {}", signupRequest);

        // 중복 이메일 확인
        String email = signupRequest.email();
        if (userRepository.existsByEmail(email)) {
            throw DuplicateEmailException.withEmail(email);
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(signupRequest.password());

        // 유저 생성 및 저장
//        User user = new User(email, signupRequest.nickname(), encodedPassword);
        User user = new User(email, signupRequest.name(), encodedPassword);
        userRepository.save(user);

        log.info("사용자 생성 완료: email={}, nickname={}", user.getEmail(), user.getNickname());

        return userMapper.toDto(user);
    }

    @Override
    public JwtInformation refreshToken(String refreshToken) {
        // Validate refresh token
        if (!tokenProvider.validateRefreshToken(refreshToken)) {
            throw new VomException(ErrorCode.INVALID_TOKEN);
        }

        String email = tokenProvider.getEmailFromToken(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        if (!(userDetails instanceof VomUserDetails vomUserDetails)) {
            throw new VomException(ErrorCode.INVALID_USER_DETAILS);
        }

        try {
            String newAccessToken = tokenProvider.generateAccessToken(vomUserDetails);
            String newRefreshToken = tokenProvider.generateRefreshToken(vomUserDetails);
            log.info("Access token refreshed for user: {}", email);
            return new JwtInformation(
                vomUserDetails.getUserDto(),
                newAccessToken,
                newRefreshToken
            );
        } catch (JOSEException e) {
            log.error("Failed to generate new tokens for user: {}", email, e);
            throw new VomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
