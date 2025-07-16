package kg.attractor.bookingsaas.service;

import java.time.LocalTime;

public interface ScheduleValidator {
    void checkScheduleExistsById(Long scheduleId);

    boolean checkForWorkTimeConflicts(Long scheduleId, LocalTime startedAtTime, LocalTime finishedAtTime);
}
