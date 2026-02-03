package okodee.vom.domain.profile.dto;

import lombok.Builder;

@Builder
public record KeywordResponse(
    Long id,
    String name,
    String category,
    String categoryDescription,
    Integer displayOrder
) {
}
