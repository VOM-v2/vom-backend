package okodee.vom.domain.dm.entity;

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
@Table(name = "direct_message_rooms")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DirectMessageRoom extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    public static DirectMessageRoom create(User sender, User receiver) {
        DirectMessageRoom room = new DirectMessageRoom();
        room.sender = sender;
        room.receiver = receiver;
        return room;
    }
}
