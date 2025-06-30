package kg.attractor.bookingsaas.dto.mapper;

import kg.attractor.bookingsaas.dto.HolidayDto;
import kg.attractor.bookingsaas.models.Holiday;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HolidayMapper {
    HolidayDto mapToDto(Holiday holiday);
    Holiday mapToEntity(HolidayDto holidayDto);
}
