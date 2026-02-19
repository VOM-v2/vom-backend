package okodee.vom.domain.snap.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import okodee.vom.domain.user.entity.User;
import okodee.vom.global.common.BaseEntity;

@Entity
@Table(name = "snaps")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Snap extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "content", length = 20)
    private String content;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String snapImageUrl;

    public Snap(User user, String content, String url) {
        this.user = user;
        this.content = content;
        this.snapImageUrl = url;
    }
}
