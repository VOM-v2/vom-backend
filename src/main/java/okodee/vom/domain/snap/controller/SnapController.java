package okodee.vom.domain.snap.controller;

import jakarta.validation.Valid;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okodee.vom.domain.snap.dto.SnapCreateRequest;
import okodee.vom.domain.snap.dto.SnapDto;
import okodee.vom.domain.snap.service.SnapService;
import okodee.vom.global.common.PageResponse;
import okodee.vom.global.security.VomUserDetails;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/snaps")
@RequiredArgsConstructor
public class SnapController {

    private final SnapService snapService;

    @PostMapping(
        path = "/me",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<SnapDto> create(
        Authentication authentication,
        @RequestPart("request") @Valid SnapCreateRequest request,
        @RequestPart(value = "image") MultipartFile image
    ) {
        UUID userId = extractUserIdFromAuthentication(authentication);

        log.info("스냅 생성 요청: userId={}, request={}", userId, request);

        return ResponseEntity.ok(snapService.create(userId, request, image));
    }

    @GetMapping
    public ResponseEntity<PageResponse<SnapDto>> findAllByUserId(
        @RequestParam("userId") UUID userId,
        @RequestParam(value = "cursor", required=false) Instant cursor,
        @PageableDefault(
            size = 50,
            page = 0,
            sort = "createdAt",
            direction = Direction.DESC
        ) Pageable pageable) {
        log.info("사용자별 스냅 목록 조회 요청: userId={}, cursor={}, pageable={}", userId, cursor, pageable);

        PageResponse<SnapDto> snaps = snapService.findAllByUserId(userId, cursor, pageable);

        log.debug("사용자별 스냅 목록 조회 응답: totalElements={}", snaps.totalElements());

        return ResponseEntity.ok(snaps);
    }

    @DeleteMapping("/{snapId}")
    public ResponseEntity<Void> delete(
        @PathVariable UUID snapId,
        Authentication authentication
    ) {
        log.info("스냅 삭제 요청: id={}", snapId);

        UUID userId = extractUserIdFromAuthentication(authentication);
        snapService.delete(snapId, userId);

        log.debug("스냅 삭제 완료");
        return ResponseEntity.noContent().build();
    }

    /**
     * Authentication 객체에서 사용자 ID 추출
     */
    private UUID extractUserIdFromAuthentication(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new IllegalStateException("인증 정보가 없습니다.");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof VomUserDetails vomUserDetails) {
            return vomUserDetails.getId();
        }

        throw new IllegalStateException("지원하지 않는 Principal 타입입니다: " + principal.getClass());
    }

}
