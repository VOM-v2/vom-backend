package okodee.vom.domain.snap.mapper;

import okodee.vom.domain.snap.dto.SnapDto;
import okodee.vom.domain.snap.entity.Snap;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SnapMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.nickname", target = "nickname")
    SnapDto toDto(Snap snap);
}
