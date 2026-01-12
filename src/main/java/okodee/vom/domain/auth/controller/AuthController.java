package okodee.vom.domain.auth.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okodee.vom.domain.auth.dto.SignupRequest;
import okodee.vom.domain.user.dto.UserDto;
import okodee.vom.domain.auth.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController implements AuthApi {
    private final AuthService authService;

    @PostMapping(path="/signup")
    public ResponseEntity<UserDto> signup(
        @RequestBody @Valid SignupRequest signupRequest
    ) {
        log.info("사용자 생성 요청: {}", signupRequest);

        UserDto createdUser = authService.signup(signupRequest);

        log.debug("사용자 생성 응답: {}", createdUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping("/csrf-token")
    public ResponseEntity<Void> getCsrfToken(CsrfToken csrfToken) {
        log.debug("CSRF 토큰 요청");
        log.trace("CSRF 토큰: {}", csrfToken.getToken());
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();
    }
}
