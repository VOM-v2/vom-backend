package okodee.vom.domain.profile.dto;

import java.time.Instant;
import java.util.UUID;

public record UserKeywordResponse(
    UUID id,
    KeywordResponse keyword,
    Instant createdAt
) {

}
