package kg.attractor.bookingsaas.dto.mapper;

import kg.attractor.bookingsaas.dto.user.OutputUserDto;
import kg.attractor.bookingsaas.models.User;
import kg.attractor.bookingsaas.projection.UserInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OutputUserMapper {

    @Mapping(target = "roleName", source = "role.roleName")
    OutputUserDto mapToDto(User user);

    @Mapping(target = "role.roleName", source = "roleName")
    User mapToEntity(OutputUserDto dto);

    @Mapping(target = "roleName", source = "role.roleName")
    OutputUserDto mapToDto(UserInfo userInfo);
}
