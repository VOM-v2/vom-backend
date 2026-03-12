package okodee.vom.domain.dm.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record DirectMessageRoomCreateRequest(
    @NotNull
    UUID receiverId
) {

}
