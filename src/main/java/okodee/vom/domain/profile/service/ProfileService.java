package okodee.vom.domain.profile.service;

import java.util.UUID;
import okodee.vom.domain.profile.dto.ProfileDto;

public interface ProfileService {
    ProfileDto find(UUID userId);
}
