package okodee.vom.domain.profile.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import okodee.vom.global.common.LongBaseEntity;

@Entity
@Table(name = "keywords")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Keyword extends LongBaseEntity {
    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private KeywordCategory category;

    @Column(nullable = false, name = "display_order")
    private Integer displayOrder;

    // 카테고리별 키워드 관리를 위한 Enum
    @Getter
    public enum KeywordCategory {
        DIGITAL("디지털"),
        CREATIVE("창작"),
        LIFESTYLE("라이프스타일"),
        HOBBY("취미"),
        MUSIC("음악"),
        LEARNING("학습");

        private final String description;

        KeywordCategory(String description) {
            this.description = description;
        }
    }
}
