package kg.attractor.bookingsaas.dto.mapper;

import kg.attractor.bookingsaas.dto.DayOfWeekDto;
import kg.attractor.bookingsaas.models.DayOfWeekEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DayOfWeekMapper {
    DayOfWeekDto toDto(DayOfWeekEntity dayOfWeek);
    DayOfWeekEntity toEntity(DayOfWeekDto dayOfWeekDto);
}
