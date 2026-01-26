package okodee.vom.domain.profile.controller;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okodee.vom.domain.profile.dto.UserKeywordResponse;
import okodee.vom.domain.profile.service.KeywordServiceImpl;
import okodee.vom.global.security.VomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/keywords")
@RequiredArgsConstructor
public class KeywordController implements KeywordApi {

    private final KeywordServiceImpl keywordService;

    @Override
    @GetMapping("/me")
    public ResponseEntity<List<UserKeywordResponse>> getMyKeywords(
        Authentication authentication
    ) {
        // Authentication에서 userId 추출
        UUID userId = extractUserIdFromAuthentication(authentication);

        log.info("사용자 관심 키워드 조회 요청 수신: userId={}", userId);

        List<UserKeywordResponse> keywords = keywordService.getUserKeywords(userId);

        log.debug("사용자 관심 키워드 조회 응답: userId={}, count={}", userId, keywords.size());

        return ResponseEntity.ok(keywords);
    }

    /**
     * Authentication 객체에서 사용자 ID 추출
     */
    private UUID extractUserIdFromAuthentication(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new IllegalStateException("인증 정보가 없습니다.");
        }

        Object principal = authentication.getPrincipal();

        // VomUserDetails에서 ID 추출
        if (principal instanceof VomUserDetails vomUserDetails) {
            return vomUserDetails.getId();  // ✅ getId() 메서드 사용
        }

        throw new IllegalStateException("지원하지 않는 Principal 타입입니다: " + principal.getClass());
    }
}
