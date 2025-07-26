package kg.attractor.bookingsaas.service;

import kg.attractor.bookingsaas.dto.BreakPeriodDto;

import java.util.List;
import java.util.Optional;

public interface BreakPeriodService {
    BreakPeriodDto getBreakPeriodById(Long id);

    Optional<BreakPeriodDto> findBreakPeriodById(Long id);

    List<BreakPeriodDto> findBreakPeriodByScheduleId(Long scheduleId);
}
