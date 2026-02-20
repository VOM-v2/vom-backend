package okodee.vom.domain.snap.service;

import java.time.Instant;
import java.util.UUID;
import okodee.vom.domain.snap.dto.SnapCreateRequest;
import okodee.vom.domain.snap.dto.SnapDto;
import okodee.vom.global.common.PageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface SnapService {
    SnapDto create(UUID userId, SnapCreateRequest snapCreateRequest, MultipartFile image);
    PageResponse<SnapDto> findAllByUserId(UUID userId, Instant createdAt, Pageable pageable);
    void delete(UUID snapId, UUID requestUserId);
}
