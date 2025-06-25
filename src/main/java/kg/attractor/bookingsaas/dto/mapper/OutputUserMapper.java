package kg.attractor.bookingsaas.dto.mapper;

import kg.attractor.bookingsaas.dto.user.OutputUserDto;
import kg.attractor.bookingsaas.models.User;
import kg.attractor.bookingsaas.projection.UserBusinessServiceProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OutputUserMapper {
    OutputUserDto mapToDto(User user);
    User mapToEntity(OutputUserDto dto);

    @Mapping(target = "roleName", source = "role.roleName")
    OutputUserDto mapToDto(UserBusinessServiceProjection.UserInfo userInfo);
}
