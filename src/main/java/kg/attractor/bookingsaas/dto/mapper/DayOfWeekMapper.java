package kg.attractor.bookingsaas.dto.mapper;

import kg.attractor.bookingsaas.dto.DayOfWeekDto;
import kg.attractor.bookingsaas.models.DayOfWeek;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DayOfWeekMapper {
    DayOfWeekDto toDto(DayOfWeek dayOfWeek);
    DayOfWeek toEntity(DayOfWeekDto dayOfWeekDto);
}
