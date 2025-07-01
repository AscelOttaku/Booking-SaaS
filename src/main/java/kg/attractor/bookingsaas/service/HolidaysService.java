package kg.attractor.bookingsaas.service;

import kg.attractor.bookingsaas.dto.HolidayDto;

import java.util.List;

public interface HolidaysService {
    List<HolidayDto> getHolidaysByYearFromDb(int year);

    HolidayDto createHoliday(HolidayDto holidayDto);

    HolidayDto updateHoliday(HolidayDto holidayDto);

    boolean existsByName(String value);

    void deleteHolidayByName(String name);

    void clearCache();
}
