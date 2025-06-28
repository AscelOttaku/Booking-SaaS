package kg.attractor.bookingsaas.service;

import kg.attractor.bookingsaas.dto.DayOfWeekDto;

public interface DayOfWeekService {
    DayOfWeekDto findDayOfWeekById(Long id);
}
