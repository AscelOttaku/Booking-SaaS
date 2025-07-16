package kg.attractor.bookingsaas.service.impl;

import kg.attractor.bookingsaas.dto.HolidayDto;
import kg.attractor.bookingsaas.dto.mapper.HolidayMapper;
import kg.attractor.bookingsaas.models.Holiday;
import kg.attractor.bookingsaas.repository.HolidayRepository;
import kg.attractor.bookingsaas.service.HolidaysService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HolidaysServiceImpl implements HolidaysService {
    private static final String COUNTRY_CODE = "KZ";
    private final WebClient webClient;
    private final HolidayRepository holidayRepository;
    private final HolidayMapper holidayMapper;
    private final CacheManager cacheManager;

    @Cacheable(value = "holidays", key = "#year")
    @Override
    public List<HolidayDto> getHolidaysByYearFromDb(int year) {
        List<HolidayDto> holidayDtoList = webClient.get()
                .uri("/{year}/{countryCode}", year, COUNTRY_CODE)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<HolidayDto>>() {
                })
                .blockOptional()
                .orElse(List.of());

        var localHolidays = getHolidaysByYear(year);

        List<HolidayDto> merged = new ArrayList<>(holidayDtoList);
        merged.addAll(localHolidays);
        return merged;
    }

    @CachePut(value = "holidays", key = "#holidayDto.date.year")
    @Override
    public HolidayDto createHoliday(HolidayDto holidayDto) {
        Holiday holiday = holidayMapper.mapToEntity(holidayDto);
        return holidayMapper.mapToDto(holidayRepository.save(holiday));
    }

    @CachePut(value = "holidays", key = "#holidayDto.date.year")
    @Override
    public HolidayDto updateHoliday(HolidayDto holidayDto) {
        Assert.isTrue(holidayDto.getId() != null && holidayDto.getId() > 0, "Id must not be null and positive");
        Holiday holiday = holidayMapper.mapToEntity(holidayDto);
        return holidayMapper.mapToDto(holidayRepository.save(holiday));
    }

    @Override
    public boolean existsByName(String value) {
        Assert.isTrue(value != null && !value.isBlank(), "Value must not be null or blank");
        return holidayRepository.existsByName(value);
    }

    private List<HolidayDto> getHolidaysByYear(int year) {
        return holidayRepository.findByYear(year).stream()
                .map(holidayMapper::mapToDto)
                .toList();
    }

    @Override
    public void deleteHolidayByName(String name) {
        Assert.isTrue(name != null && !name.isBlank(), "Name must not be null or blank");
        Holiday holiday = holidayRepository.findByName(name)
                .orElseThrow(() -> new NoSuchElementException("Holiday with name '" + name + "' not found"));
        holidayRepository.delete(holiday);

        Cache cache = cacheManager.getCache("holidays");
        if (cache != null) {
            Cache.ValueWrapper valueWrapper = cache.get(holiday.getDate().getYear());
            if (valueWrapper != null) {
                Object cacheValue = valueWrapper.get();
                if (cacheValue instanceof List<?> cacheList && cacheList.stream().allMatch(HolidayDto.class::isInstance)) {
                    @SuppressWarnings("unchecked")
                    List<HolidayDto> holidayDtoList = (List<HolidayDto>) cacheList;
                    holidayDtoList.removeIf(h -> h.getName().equals(name));
                    cache.put(holiday.getDate().getYear(), holidayDtoList);
                }
            }
        }
    }

    @Override
    public void clearCache() {
        Optional.ofNullable(cacheManager.getCache("holidays"))
                .ifPresent(Cache::clear);
    }
}
