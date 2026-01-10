package okodee.vom.domain.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okodee.vom.domain.auth.dto.SignupRequest;
import okodee.vom.domain.user.dto.UserDto;
import okodee.vom.domain.user.entity.User;
import okodee.vom.domain.auth.exception.DuplicateEmailException;
import okodee.vom.domain.user.mapper.UserMapper;
import okodee.vom.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    @Override
    public UserDto signup(SignupRequest signupRequest) {
        log.debug("사용자 생성 시작: {}", signupRequest);

        // 중복 이메일 확인
        String email = signupRequest.email();
        if (userRepository.existsByEmail(email)) {
            throw DuplicateEmailException.withEmail(email);
        }

        // 유저 생성 및 저장
        User user = new User(email, signupRequest.nickname(), signupRequest.password());
        userRepository.save(user);

        log.info("사용자 생성 완료: email={}, nickname={}", user.getEmail(), user.getNickname());

        return userMapper.toDto(user);
    }
}
