package okodee.vom.domain.dm.repository;

import java.util.Optional;
import java.util.UUID;
import okodee.vom.domain.dm.entity.DirectMessageRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DirectMessageRoomRepository extends JpaRepository<DirectMessageRoom, UUID> {

    Optional<DirectMessageRoom> findBySenderIdAndReceiverId(UUID senderId, UUID receiverId);
}
