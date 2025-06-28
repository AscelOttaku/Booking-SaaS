package kg.attractor.bookingsaas.dto.mapper;

import kg.attractor.bookingsaas.dto.BreakPeriodDto;
import kg.attractor.bookingsaas.models.BreakPeriod;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BreakPeriodMapper {

    @Mapping(target = "scheduleSettingsId", source = "settings.id")
    @Mapping(target = "start", source = "start")
    @Mapping(target = "end", source = "end")
    BreakPeriodDto toDto(BreakPeriod breakPeriod);

    @Mapping(target = "settings.id", source = "scheduleSettingsId")
    @Mapping(target = "start", source = "start")
    @Mapping(target = "end", source = "end")
    BreakPeriod toEntity(BreakPeriodDto breakPeriodDto);
}
