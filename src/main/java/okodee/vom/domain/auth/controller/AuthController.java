package okodee.vom.domain.auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okodee.vom.domain.auth.dto.JwtDto;
import okodee.vom.domain.auth.dto.JwtInformation;
import okodee.vom.domain.auth.dto.SignupRequest;
import okodee.vom.domain.auth.service.AuthService;
import okodee.vom.domain.user.dto.UserDto;
import okodee.vom.global.security.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController implements AuthApi {
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping(path="/users")
//    @PostMapping(path="/signup")
    public ResponseEntity<UserDto> signup(
        @RequestBody @Valid SignupRequest signupRequest
    ) {
        log.info("사용자 생성 요청: email={}", signupRequest.email());

        UserDto createdUser = authService.signup(signupRequest);

        log.debug("사용자 생성 응답: {}", createdUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping("/auth/csrf-token")
    public ResponseEntity<Void> getCsrfToken(CsrfToken csrfToken) {
        log.debug("CSRF 토큰 요청");
        log.trace("CSRF 토큰 발급");
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<JwtDto> refresh(@CookieValue("REFRESH_TOKEN") String refreshToken,
        HttpServletResponse response) {
        log.info("토큰 리프레시 요청");
        JwtInformation refreshResult = authService.refreshToken(refreshToken);
        Cookie refreshCookie = jwtTokenProvider.generateRefreshTokenCookie(
            refreshResult.refreshToken());
        response.addCookie(refreshCookie);

        JwtDto body = new JwtDto(

            refreshResult.userDto(),
            refreshResult.accessToken()
        );
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(body);
    }
}
