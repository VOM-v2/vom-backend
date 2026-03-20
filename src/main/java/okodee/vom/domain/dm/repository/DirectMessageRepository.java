package okodee.vom.domain.dm.repository;

import java.util.List;
import java.util.UUID;
import okodee.vom.domain.dm.entity.DirectMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DirectMessageRepository extends JpaRepository<DirectMessage, UUID> {

    Page<DirectMessage> findByRoomIdOrderByCreatedAtAsc(UUID roomId, Pageable pageable);

    // 특정 방에서 내가 받은 메시지 중 읽지 않은 것만 조회
    List<DirectMessage> findAllByRoomIdAndSenderIdNotAndIsReadFalse(UUID roomId, UUID senderId);

    long countByRoomIdAndSenderIdNotAndIsReadFalse(UUID roomId, UUID senderId);
}
