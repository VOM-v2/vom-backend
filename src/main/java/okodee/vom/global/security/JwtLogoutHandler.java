package okodee.vom.global.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {

    private final JwtTokenProvider tokenProvider;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) {

        // Clear refresh token cookie
        ResponseCookie refreshTokenExpirationCookie = tokenProvider.generateRefreshTokenExpirationCookie();
        response.addHeader("Set-Cookie", refreshTokenExpirationCookie.toString());

        log.debug("JWT logout handler executed - refresh token cookie cleared");
    }
}