package okodee.vom.domain.snap.service;

import java.util.UUID;
import okodee.vom.domain.snap.dto.SnapCreateRequest;
import okodee.vom.domain.snap.dto.SnapDto;
import org.springframework.web.multipart.MultipartFile;

public interface SnapService {
    SnapDto create(UUID userId, SnapCreateRequest snapCreateRequest, MultipartFile image);
}
