package okodee.vom.domain.profile.exception;

import okodee.vom.global.exception.ErrorCode;

/**
 * 최대 키워드 개수 초과 예외
 *
 * 사용자가 5개를 초과하는 키워드를 설정하려고 할 때 발생합니다.
 */
public class MaxKeywordsExceededException extends KeywordException {

    private static final int MAX_KEYWORDS = 5;

    public MaxKeywordsExceededException() {
        super(ErrorCode.MAX_KEYWORDS_EXCEEDED);
        this.addDetail("maxKeywords", MAX_KEYWORDS);
    }

    /**
     * 시도한 키워드 개수를 포함하는 생성자
     *
     * @param attemptedCount 설정하려고 시도한 키워드 개수
     */
    public MaxKeywordsExceededException(int attemptedCount) {
        super(ErrorCode.MAX_KEYWORDS_EXCEEDED);
        this.addDetail("maxKeywords", MAX_KEYWORDS);
        this.addDetail("attemptedCount", attemptedCount);
    }
}
