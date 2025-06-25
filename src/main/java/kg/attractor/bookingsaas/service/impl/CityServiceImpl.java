package kg.attractor.bookingsaas.service.impl;

import kg.attractor.bookingsaas.dto.CityDto;
import kg.attractor.bookingsaas.dto.mapper.CityMapper;
import kg.attractor.bookingsaas.repository.CityRepository;
import kg.attractor.bookingsaas.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;
    private final CityMapper cityMapper;

    @Override
    public List<CityDto> findAllCities() {
        return cityRepository.findAll()
                .stream()
                .map(cityMapper::mapToDto)
                .toList();
    }
}
