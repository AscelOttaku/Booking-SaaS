package kg.attractor.bookingsaas.service;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import kg.attractor.bookingsaas.dto.DailyScheduleDto;
import kg.attractor.bookingsaas.dto.ScheduleTimeDto;
import kg.attractor.bookingsaas.dto.WeeklyScheduleDto;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

public interface ScheduleService {
    DailyScheduleDto createDailySchedule(DailyScheduleDto dailyScheduleDto);

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    WeeklyScheduleDto createWeeklySchedule(WeeklyScheduleDto weeklyScheduleDto);

    DailyScheduleDto updateDailySchedule(DailyScheduleDto dailyScheduleDto);

    WeeklyScheduleDto updateWeeklySchedule(WeeklyScheduleDto weeklyScheduleDto);

    Long findMaxBookingSizeByScheduleId(Long scheduleId);

    ScheduleTimeDto findScheduleTimeById(Long id);

    void deleteDailyScheduleById(Long id);

    int findDurationBetweenBooksByScheduleId(Long scheduleId);
}
