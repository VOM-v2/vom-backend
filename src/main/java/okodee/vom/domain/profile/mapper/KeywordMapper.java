package okodee.vom.domain.profile.mapper;

import okodee.vom.domain.profile.dto.KeywordResponse;
import okodee.vom.domain.profile.entity.Keyword;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface KeywordMapper {
    /**
     * Keyword Entity -> KeywordResponse DTO 변환
     *
     * category는 Enum이므로 String으로 변환 필요
     * categoryDescription은 Enum의 description 필드를 매핑
     */
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryToString")
    @Mapping(target = "categoryDescription", source = "category", qualifiedByName = "categoryToDescription")
    KeywordResponse toResponse(Keyword keyword);

    /**
     * Enum -> String 변환 (카테고리명)
     */
    @Named("categoryToString")
    default String categoryToString(Keyword.KeywordCategory category) {
        return category != null ? category.name() : null;
    }

    /**
     * Enum -> String 변환 (카테고리 설명)
     */
    @Named("categoryToDescription")
    default String categoryToDescription(Keyword.KeywordCategory category) {
        return category != null ? category.getDescription() : null;
    }
}
