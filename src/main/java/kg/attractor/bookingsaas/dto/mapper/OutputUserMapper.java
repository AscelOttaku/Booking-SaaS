package kg.attractor.bookingsaas.dto.mapper;

import kg.attractor.bookingsaas.dto.OutputUserDto;
import kg.attractor.bookingsaas.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OutputUserMapper {
    OutputUserDto mapToDto(User user);
    User mapToEntity(OutputUserDto dto);
}
