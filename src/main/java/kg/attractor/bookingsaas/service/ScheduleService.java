package kg.attractor.bookingsaas.service;

import kg.attractor.bookingsaas.dto.DailyScheduleDto;
import kg.attractor.bookingsaas.dto.WeeklyScheduleDto;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface ScheduleService {
    DailyScheduleDto createDailySchedule(DailyScheduleDto dailyScheduleDto);

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    WeeklyScheduleDto createWeeklySchedule(WeeklyScheduleDto weeklyScheduleDto);

    Long findMaxBookingSizeByScheduleId(Long scheduleId);
}
