package okodee.vom.domain.profile.service;

import java.util.List;
import java.util.UUID;
import okodee.vom.domain.profile.dto.UserKeywordResponse;

public interface KeywordService {
    List<UserKeywordResponse> getUserKeywords(UUID userId);
}
