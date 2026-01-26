package okodee.vom.domain.profile.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import okodee.vom.global.common.BaseEntity;

@Entity
@Table(
    name = "user_keywords",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "keyword_id"})
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserKeyword extends BaseEntity {
    @Column(nullable = false, name = "user_id")
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keyword_id", nullable = false)
    private Keyword keyword;

    @Builder
    public UserKeyword(UUID userId, Keyword keyword) {
        this.userId = userId;
        this.keyword = keyword;
    }

    // 비즈니스 로직: 동일한 사용자의 키워드인지 확인
    public boolean isOwnedBy(UUID userId) {
        return this.userId.equals(userId);
    }
}
