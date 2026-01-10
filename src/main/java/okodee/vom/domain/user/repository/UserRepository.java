package okodee.vom.domain.user.repository;

import java.util.UUID;
import okodee.vom.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);
}
