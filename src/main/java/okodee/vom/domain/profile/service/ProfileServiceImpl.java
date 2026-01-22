package okodee.vom.domain.profile.service;

import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okodee.vom.domain.profile.dto.ProfileDto;
import okodee.vom.domain.profile.dto.ProfileUpdateRequest;
import okodee.vom.domain.user.entity.User;
import okodee.vom.domain.user.exception.UserNotFoundException;
import okodee.vom.domain.user.mapper.UserMapper;
import okodee.vom.domain.user.repository.UserRepository;
import okodee.vom.global.util.S3ImageStorage;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final S3ImageStorage s3ImageStorage;

    @Override
    public ProfileDto find(UUID userId) {
        return userRepository.findById(userId)
            .map(userMapper::toProfileDto)
            .orElseThrow(() -> UserNotFoundException.withId(userId));
    }

    @PreAuthorize("principal.userDto.id == #userId")
    @Transactional
    @Override
    public ProfileDto update(UUID userId, ProfileUpdateRequest profileUpdateRequest, Optional<MultipartFile> image) {
        log.debug("사용자 프로필 수정 시작: id={}", userId);

        User user = userRepository.findById(userId)
            .orElseThrow(() -> {
                UserNotFoundException exception = UserNotFoundException.withId(userId);
                return exception;
            });

        String newProfileImageUrl = image.map(img -> {
            log.debug("프로필 이미지 업로드 시작");
            return s3ImageStorage.uploadImage(img, "profileImage/");
        }).orElse(null);

        user.update(
            profileUpdateRequest.name(),
            profileUpdateRequest.gender(),
            profileUpdateRequest.birthDate(),
            newProfileImageUrl
        );

        log.info("사용자 프로필 수정 완료: id={}", userId);

        return userMapper.toProfileDto(user);
    }
}
