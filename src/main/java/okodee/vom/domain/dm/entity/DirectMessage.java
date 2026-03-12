package okodee.vom.domain.dm.entity;

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
@Table(name = "direct_messages")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DirectMessage extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private DirectMessageRoom room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean isRead = false;

    public static DirectMessage create(DirectMessageRoom room, User sender, String content) {
        DirectMessage message = new DirectMessage();
        message.room = room;
        message.sender = sender;
        message.content = content;
        return message;
    }

    public void markAsRead() {
        this.isRead = true;
    }
}
