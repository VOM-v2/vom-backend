package okodee.vom.domain.snap.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okodee.vom.domain.snap.dto.SnapCreateRequest;
import okodee.vom.domain.snap.dto.SnapDto;
import okodee.vom.domain.snap.entity.Snap;
import okodee.vom.domain.snap.mapper.SnapMapper;
import okodee.vom.domain.snap.repository.SnapRepository;
import okodee.vom.domain.user.entity.User;
import okodee.vom.domain.user.exception.UserNotFoundException;
import okodee.vom.domain.user.repository.UserRepository;
import okodee.vom.global.common.PageResponse;
import okodee.vom.global.common.PageResponseMapper;
import okodee.vom.global.util.S3ImageStorage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class SnapServiceImpl implements SnapService {

    private final UserRepository userRepository;
    private final SnapRepository snapRepository;
    private final S3ImageStorage s3ImageStorage;
    private final SnapMapper snapMapper;
    private final PageResponseMapper pageResponseMapper;

    @Transactional
    @Override
    public SnapDto create(UUID userId, SnapCreateRequest snapCreateRequest, MultipartFile image) {
        log.debug("스냅 생성 시작: request={}", snapCreateRequest);

        User user = userRepository.findById(userId)
            .orElseThrow(() -> UserNotFoundException.withId(userId));

        log.debug("스냅 이미지 업로드 시작");
        String snapImageUrl = s3ImageStorage.uploadImage(image, "snapImage/");

        String content = snapCreateRequest.content();
        Snap snap = new Snap(
            user,
            content,
            snapImageUrl
        );

        snapRepository.save(snap);

        log.info("스냅 생성 완료: id={}, userId={}", snap.getId(), userId);

        return snapMapper.toDto(snap);
    }

    @Override
    public PageResponse<SnapDto> findAllByUserId(UUID userId, Instant createdAt, Pageable pageable) {
        Slice<SnapDto> slice = snapRepository.findAllByUserId(userId,
                Optional.ofNullable(createdAt).orElse(Instant.now()),
                pageable)
            .map(snapMapper::toDto);

        Instant nextCursor = null;
        if (!slice.getContent().isEmpty()) {
            nextCursor = slice.getContent().get(slice.getContent().size() - 1)
                .createdAt();
        }
        return pageResponseMapper.fromSlice(slice, nextCursor);
    }
}
