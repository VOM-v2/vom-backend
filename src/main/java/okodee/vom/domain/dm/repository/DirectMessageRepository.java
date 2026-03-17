package okodee.vom.domain.dm.repository;

import java.util.List;
import java.util.UUID;
import okodee.vom.domain.dm.entity.DirectMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DirectMessageRepository extends JpaRepository<DirectMessage, UUID> {

    List<DirectMessage> findAllByRoomIdOrderByCreatedAtAsc(UUID roomId);
}
