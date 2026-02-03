package okodee.vom.domain.profile.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record KeywordUpdateRequest(

    @NotNull(message = "키워드 ID 목록은 필수입니다.")
    @Size(max = 5, message = "관심 키워드는 최대 5개까지 설정할 수 있습니다.")
    List<Long> keywordIds
) {

}
