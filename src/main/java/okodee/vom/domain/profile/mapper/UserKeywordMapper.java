package okodee.vom.domain.profile.mapper;

import okodee.vom.domain.profile.dto.UserKeywordResponse;
import okodee.vom.domain.profile.entity.UserKeyword;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = KeywordMapper.class
)
public interface UserKeywordMapper {
    /**
     * UserKeyword Entity -> UserKeywordResponse DTO 변환
     *
     * keyword 필드는 KeywordMapper를 사용해서 자동 변환
     */
    @Mapping(target = "keyword", source = "keyword")
    UserKeywordResponse toResponse(UserKeyword userKeyword);
}
