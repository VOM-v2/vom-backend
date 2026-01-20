package okodee.vom.domain.profile.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okodee.vom.domain.profile.dto.ProfileDto;
import okodee.vom.domain.user.exception.UserNotFoundException;
import okodee.vom.domain.user.mapper.UserMapper;
import okodee.vom.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public ProfileDto find(UUID userId) {
        return userRepository.findById(userId)
            .map(userMapper::toProfileDto)
            .orElseThrow(() -> UserNotFoundException.withId(userId));
    }
}
