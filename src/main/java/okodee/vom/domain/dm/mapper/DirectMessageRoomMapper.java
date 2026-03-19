package okodee.vom.domain.dm.mapper;

import java.util.UUID;
import okodee.vom.domain.dm.dto.DirectMessageRoomListResponse;
import okodee.vom.domain.dm.dto.DirectMessageRoomResponse;
import okodee.vom.domain.dm.entity.DirectMessageRoom;
import okodee.vom.domain.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DirectMessageRoomMapper {

    @Mapping(source = "id", target = "roomId")
    @Mapping(source = "receiver.id", target = "receiverId")
    @Mapping(source = "receiver.nickname", target = "receiverNickname")
    DirectMessageRoomResponse toResponse(DirectMessageRoom room);

    default DirectMessageRoomListResponse toListResponse(DirectMessageRoom room, UUID currentUserId, long unreadCount) {
        boolean isSender = room.getSender().getId().equals(currentUserId);
        User partner = isSender ? room.getReceiver() : room.getSender();
        String partnerNickname = partner.getNickname() != null ? partner.getNickname() : "알 수 없음";

        return new DirectMessageRoomListResponse(
            room.getId(),
            partner.getId(),
            partnerNickname,
            unreadCount
        );
    }
}
