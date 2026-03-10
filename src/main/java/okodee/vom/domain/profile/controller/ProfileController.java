package okodee.vom.domain.profile.controller;

import jakarta.validation.Valid;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okodee.vom.domain.profile.dto.ProfileDto;
import okodee.vom.domain.profile.dto.ProfileUpdateRequest;
import okodee.vom.domain.profile.service.ProfileService;
import okodee.vom.global.security.VomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProfileController implements ProfileApi {

    private final ProfileService profileService;

    @GetMapping(path = "/users/{userId}/profiles")
    public ResponseEntity<ProfileDto> find(@PathVariable("userId") UUID userId) {
        log.info("사용자 프로필 조회 요청 수신");

        ProfileDto profile = profileService.find(userId);

        log.debug("사용자 프로필 조회 응답: {}", userId);

        return ResponseEntity.status(HttpStatus.OK).body(profile);
    }

    @Override
    @PatchMapping(
        path = "/profiles/me",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ProfileDto> updateMyProfile(
        Authentication authentication,
        @RequestPart("request") @Valid ProfileUpdateRequest request,
        @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        UUID userId = extractUserIdFromAuthentication(authentication);

        log.info("[NEW] 사용자 프로필 수정 요청: userId={}, request={}", userId, request);

        return updateProfileInternal(userId, request, image);
    }

    // ==================== Private Methods (공통 로직) ====================

    /**
     * 프로필 수정 공통 로직
     */
    private ResponseEntity<ProfileDto> updateProfileInternal(
        UUID userId,
        ProfileUpdateRequest request,
        MultipartFile image
    ) {
        ProfileDto updatedProfile = profileService.update(
            userId,
            request,
            Optional.ofNullable(image)
        );

        log.debug("사용자 프로필 수정 응답: {}", updatedProfile);

        return ResponseEntity.ok()
            .header("X-API-Version", "2.0")
            .body(updatedProfile);
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
