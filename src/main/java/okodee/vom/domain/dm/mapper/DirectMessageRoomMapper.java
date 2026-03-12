package okodee.vom.domain.dm.mapper;

import okodee.vom.domain.dm.dto.DirectMessageRoomResponse;
import okodee.vom.domain.dm.entity.DirectMessageRoom;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DirectMessageRoomMapper {

    @Mapping(source = "id", target = "roomId")
    @Mapping(source = "receiver.id", target = "receiverId")
    @Mapping(source = "receiver.nickname", target = "receiverNickname")
    DirectMessageRoomResponse toResponse(DirectMessageRoom room);
}
