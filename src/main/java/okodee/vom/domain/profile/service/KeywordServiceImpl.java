package okodee.vom.domain.profile.service;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okodee.vom.domain.profile.dto.UserKeywordResponse;
import okodee.vom.domain.profile.mapper.UserKeywordMapper;
import okodee.vom.domain.profile.repository.UserKeywordRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KeywordServiceImpl implements KeywordService {
    private final UserKeywordRepository userKeywordRepository;
    private final UserKeywordMapper userKeywordMapper;

    /**
     * 사용자의 관심 키워드 목록 조회
     */
    @PreAuthorize("principal.id == #userId")
    @Override
    public List<UserKeywordResponse> getUserKeywords(UUID userId) {
        log.debug("Fetching keywords for user: {}", userId);

        return userKeywordRepository.findAllByUserIdWithKeyword(userId)
            .stream()
            .map(userKeywordMapper::toResponse)
            .toList();
    }
}
