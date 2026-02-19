package okodee.vom.domain.snap.repository;

import java.util.UUID;
import okodee.vom.domain.snap.entity.Snap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SnapRepository extends JpaRepository<Snap, UUID> {

}
