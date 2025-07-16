package kg.attractor.bookingsaas.dto.mapper;

import kg.attractor.bookingsaas.dto.CityDto;
import kg.attractor.bookingsaas.models.City;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CityMapper {
    City mapToEntity(CityDto cityDto);
    CityDto mapToDto(City city);
}
