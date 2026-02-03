package okodee.vom.domain.user.mapper;

import okodee.vom.domain.profile.dto.ProfileDto;
import okodee.vom.domain.user.dto.UserDto;
import okodee.vom.domain.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);

    @Mapping(target = "userId", source = "id")
    ProfileDto toProfileDto(User user);
}
