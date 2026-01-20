package okodee.vom.domain.profile.controller;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okodee.vom.domain.auth.controller.AuthApi;
import okodee.vom.domain.profile.dto.ProfileDto;
import okodee.vom.domain.profile.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
