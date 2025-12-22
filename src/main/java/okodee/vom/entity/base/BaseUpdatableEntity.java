package okodee.vom.entity.base;

import jakarta.persistence.Column;
import java.time.Instant;
import org.springframework.data.annotation.LastModifiedDate;

public abstract class BaseUpdatableEntity extends BaseEntity {
    @LastModifiedDate
    @Column(columnDefinition = "timestamp with time zone")
    protected Instant updatedAt;
}
