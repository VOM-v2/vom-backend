package okodee.vom.domain.user.mapper;

import okodee.vom.domain.user.dto.UserDto;
import okodee.vom.domain.user.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
}
