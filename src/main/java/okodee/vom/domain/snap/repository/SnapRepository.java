package okodee.vom.domain.snap.repository;

import java.time.Instant;
import java.util.UUID;
import okodee.vom.domain.snap.entity.Snap;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SnapRepository extends JpaRepository<Snap, UUID> {
    @Query("SELECT s FROM Snap s "
        + "LEFT JOIN FETCH s.user u "
        + "WHERE s.user.id=:userId AND s.createdAt < :createdAt")
    Slice<Snap> findAllByUserId(@Param("userId") UUID userId,
        @Param("createdAt") Instant createdAt,
        Pageable pageable);
}
