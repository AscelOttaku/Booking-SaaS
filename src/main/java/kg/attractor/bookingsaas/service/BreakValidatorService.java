package kg.attractor.bookingsaas.service;

import java.time.LocalTime;

public interface BreakValidatorService {
    boolean isBreakPeriodValid(Long scheduleId, LocalTime startedAt, LocalTime finishedAt);
}
