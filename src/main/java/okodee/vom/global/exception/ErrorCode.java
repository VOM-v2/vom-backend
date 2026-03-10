package okodee.vom.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // Auth
    DUPLICATE_EMAIL("이미 가입된 이메일입니다."),
    INVALID_TOKEN("토큰이 유효하지 않습니다."),
    INVALID_USER_DETAILS("사용자 인증 정보(UserDetails)가 유효하지 않습니다."),

    // User
    USER_NOT_FOUND("사용자를 찾을 수 없습니다."),
    UNAUTHORIZED_ACCESS("접근 권한이 없습니다."),

    // Keyword
    KEYWORD_NOT_FOUND("존재하지 않는 키워드입니다."),
    MAX_KEYWORDS_EXCEEDED("관심 키워드는 최대 5개까지 설정할 수 있습니다."),
    DUPLICATE_KEYWORD("이미 설정한 키워드입니다."),

    // Snap
    SNAP_NOT_FOUND("존재하지 않는 스냅입니다."),

    // Server 에러 코드
    INTERNAL_SERVER_ERROR("서버 내부 오류가 발생했습니다."),
    INVALID_REQUEST("잘못된 요청입니다.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}