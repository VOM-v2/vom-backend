package okodee.vom.domain.profile.service;

import java.util.Optional;
import java.util.UUID;
import okodee.vom.domain.profile.dto.ProfileDto;
import okodee.vom.domain.profile.dto.ProfileUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileService {
    ProfileDto find(UUID userId);
    ProfileDto update(UUID userId, ProfileUpdateRequest profileUpdateRequest, Optional<MultipartFile> image);
}
