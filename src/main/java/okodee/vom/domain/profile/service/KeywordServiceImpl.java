package okodee.vom.domain.profile.service;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okodee.vom.domain.profile.dto.KeywordUpdateRequest;
import okodee.vom.domain.profile.dto.UserKeywordResponse;
import okodee.vom.domain.profile.entity.Keyword;
import okodee.vom.domain.profile.entity.UserKeyword;
import okodee.vom.domain.profile.exception.DuplicateKeywordException;
import okodee.vom.domain.profile.exception.KeywordNotFoundException;
import okodee.vom.domain.profile.mapper.UserKeywordMapper;
import okodee.vom.domain.profile.repository.KeywordRepository;
import okodee.vom.domain.profile.repository.UserKeywordRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KeywordServiceImpl implements KeywordService {

    private final KeywordRepository keywordRepository;
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

    /**
     * 사용자의 관심 키워드 수정 (전체 교체)
     *
     * 1. 기존 키워드 모두 삭제
     * 2. 새로운 키워드 추가
     * 3. 수정된 키워드 목록 반환
     */
    @Transactional
    public List<UserKeywordResponse> updateUserKeywords(UUID userId,KeywordUpdateRequest request) {
        log.debug("Updating keywords for user: userId={}, newKeywordIds={}",
            userId, request.keywordIds());

        // 기존 키워드 모두 삭제
        userKeywordRepository.deleteAllByUserId(userId);
        log.debug("Deleted existing keywords for user: {}", userId);

        // 빈 배열인 경우 삭제만 하고 종료
        if (request.keywordIds() == null || request.keywordIds().isEmpty()) {
            log.info("All keywords removed for user: {}", userId);
            return List.of();
        }

        // 중복 키워드 검증
        validateNoDuplicateKeywords(request.keywordIds());

        // 새로운 키워드 조회
        List<Keyword> keywords = keywordRepository.findAllById(request.keywordIds());

        // 존재하지 않는 키워드 ID 검증
        validateAllKeywordsExist(request.keywordIds(), keywords);

        // 새로운 UserKeyword 엔티티 생성 및 저장
        List<UserKeyword> userKeywords = keywords.stream()
            .map(keyword -> UserKeyword.builder()
                .userId(userId)
                .keyword(keyword)
                .build())
            .toList();

        List<UserKeyword> savedUserKeywords = userKeywordRepository.saveAll(userKeywords);
        log.info("Updated keywords for user: userId={}, count={}", userId, savedUserKeywords.size());

        // 정렬된 결과 조회 및 반환
        return userKeywordRepository.findAllByUserIdWithKeyword(userId)
            .stream()
            .map(userKeywordMapper::toResponse)
            .toList();
    }

    /**
     * 중복 키워드 ID 검증
     */
    private void validateNoDuplicateKeywords(List<Long> keywordIds) {
        long distinctCount = keywordIds.stream().distinct().count();

        if (distinctCount < keywordIds.size()) {
            // 중복된 ID 찾기
            List<Long> duplicates = keywordIds.stream()
                .filter(id -> keywordIds.indexOf(id) != keywordIds.lastIndexOf(id))
                .distinct()
                .toList();

            log.error("Duplicate keyword IDs detected: {}", duplicates);
            throw new DuplicateKeywordException(duplicates);
        }
    }

    /**
     * 모든 키워드가 존재하는지 검증
     */
    private void validateAllKeywordsExist(List<Long> requestedIds, List<Keyword> foundKeywords) {
        if (foundKeywords.size() != requestedIds.size()) {
            List<Long> foundIds = foundKeywords.stream()
                .map(Keyword::getId)
                .toList();

            List<Long> notFoundIds = requestedIds.stream()
                .filter(id -> !foundIds.contains(id))
                .toList();

            log.error("Keywords not found: notFoundIds={}", notFoundIds);
            throw new KeywordNotFoundException(notFoundIds);
        }
    }
}
