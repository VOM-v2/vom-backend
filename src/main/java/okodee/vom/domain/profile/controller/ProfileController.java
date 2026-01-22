package okodee.vom.domain.profile.controller;

import jakarta.validation.Valid;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okodee.vom.domain.profile.dto.ProfileDto;
import okodee.vom.domain.profile.dto.ProfileUpdateRequest;
import okodee.vom.domain.profile.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @PatchMapping(
        path = "/users/{userId}/profiles",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ProfileDto> update(
        @PathVariable("userId") UUID userId,
        @RequestPart("request") @Valid ProfileUpdateRequest request,
        @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        log.info("사용자 프로필 수정 요청: id={}, request={}", userId, request);

        ProfileDto updatedProfile = profileService.update(userId, request, Optional.ofNullable(image));

        log.debug("사용자 프로필 수정 응답: {}", updatedProfile);

        return ResponseEntity.ok(updatedProfile);
    }
}
