package okodee.vom.domain.snap.mapper;

import okodee.vom.domain.snap.dto.SnapDto;
import okodee.vom.domain.snap.entity.Snap;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SnapMapper {
    SnapDto toDto(Snap snap);
}
