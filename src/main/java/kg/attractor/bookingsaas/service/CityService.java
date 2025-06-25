package kg.attractor.bookingsaas.service;

import kg.attractor.bookingsaas.dto.CityDto;

import java.util.List;

public interface CityService {
    List<CityDto> findAllCities();
}
