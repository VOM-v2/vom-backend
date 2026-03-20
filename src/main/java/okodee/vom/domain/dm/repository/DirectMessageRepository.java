package okodee.vom.domain.dm.repository;

import java.util.List;
import java.util.UUID;
import okodee.vom.domain.dm.entity.DirectMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DirectMessageRepository extends JpaRepository<DirectMessage, UUID> {

    Page<DirectMessage> findByRoomIdOrderByCreatedAtDesc(UUID roomId, Pageable pageable);

    // 특정 방에서 내가 받은 메시지 중 읽지 않은 것만 조회
    List<DirectMessage> findAllByRoomIdAndSenderIdNotAndIsReadFalse(UUID roomId, UUID senderId);

    @Query("SELECT m.room.id, COUNT(m) FROM DirectMessage m " +
        "WHERE m.room.id IN :roomIds " +
        "AND m.sender.id != :currentUserId " +
        "AND m.isRead = false " +
        "GROUP BY m.room.id")
    List<Object[]> countUnreadByRoomIds(
        @Param("currentUserId") UUID currentUserId,
        @Param("roomIds") List<UUID> roomIds
    );
}
