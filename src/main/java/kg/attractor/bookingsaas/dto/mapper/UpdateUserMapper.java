package kg.attractor.bookingsaas.dto.mapper;

import kg.attractor.bookingsaas.dto.user.UpdateUserDto;
import kg.attractor.bookingsaas.models.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UpdateUserMapper {
    User mapToEntity(UpdateUserDto updateUserDto);
    UpdateUserDto mapToDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUser(User user, @MappingTarget UpdateUserDto updateUserDto);
}
