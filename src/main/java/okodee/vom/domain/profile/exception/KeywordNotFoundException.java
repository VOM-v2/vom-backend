package okodee.vom.domain.profile.exception;

import java.util.List;
import okodee.vom.global.exception.ErrorCode;

public class KeywordNotFoundException extends KeywordException {

    public KeywordNotFoundException() {
        super(ErrorCode.KEYWORD_NOT_FOUND);
    }

    /**
     * 찾을 수 없는 키워드 ID 목록을 포함하는 생성자
     *
     * @param notFoundKeywordIds 존재하지 않는 키워드 ID 목록
     */
    public KeywordNotFoundException(List<Long> notFoundKeywordIds) {
        super(ErrorCode.KEYWORD_NOT_FOUND);
        this.addDetail("notFoundKeywordIds", notFoundKeywordIds);
    }

    /**
     * 단일 키워드 ID를 포함하는 생성자
     *
     * @param keywordId 존재하지 않는 키워드 ID
     */
    public KeywordNotFoundException(Long keywordId) {
        super(ErrorCode.KEYWORD_NOT_FOUND);
        this.addDetail("keywordId", keywordId);
    }
}
