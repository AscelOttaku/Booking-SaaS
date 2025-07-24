package kg.attractor.bookingsaas.service;

import kg.attractor.bookingsaas.dto.BreakPeriodDto;

import java.util.Optional;

public interface BreakPeriodService {
    BreakPeriodDto getBreakPeriodById(Long id);

    Optional<BreakPeriodDto> findBreakPeriodById(Long id);

    BreakPeriodDto findBreakPeriodByScheduleId(Long scheduleId);
}
