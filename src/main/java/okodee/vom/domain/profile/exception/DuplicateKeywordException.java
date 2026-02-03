package okodee.vom.domain.profile.exception;

import java.util.List;
import okodee.vom.global.exception.ErrorCode;

/**
 * 중복 키워드 예외
 *
 * 요청한 키워드 목록에 중복된 ID가 포함되어 있을 때 발생합니다.
 */
public class DuplicateKeywordException extends KeywordException {

    public DuplicateKeywordException() {
        super(ErrorCode.DUPLICATE_KEYWORD);
    }

    /**
     * 중복된 키워드 ID 목록을 포함하는 생성자
     *
     * @param duplicateKeywordIds 중복된 키워드 ID 목록
     */
    public DuplicateKeywordException(List<Long> duplicateKeywordIds) {
        super(ErrorCode.DUPLICATE_KEYWORD);
        this.addDetail("duplicateKeywordIds", duplicateKeywordIds);
    }

    /**
     * 단일 중복 키워드 ID를 포함하는 생성자
     *
     * @param keywordId 중복된 키워드 ID
     */
    public DuplicateKeywordException(Long keywordId) {
        super(ErrorCode.DUPLICATE_KEYWORD);
        this.addDetail("keywordId", keywordId);
    }
}
