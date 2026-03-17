package okodee.vom.domain.dm.mapper;

import okodee.vom.domain.dm.dto.DirectMessageResponse;
import okodee.vom.domain.dm.entity.DirectMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DirectMessageMapper {

    @Mapping(source = "id", target = "messageId")
    @Mapping(source = "sender.id", target = "senderId")
    DirectMessageResponse toResponse(DirectMessage message);
}
