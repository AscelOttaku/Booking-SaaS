package kg.attractor.bookingsaas.dto.mapper.impl;

import kg.attractor.bookingsaas.dto.DailyScheduleDto;
import kg.attractor.bookingsaas.models.DayOfWeek;
import kg.attractor.bookingsaas.models.Schedule;
import kg.attractor.bookingsaas.service.DayOfWeekService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleMapper {
    private final DayOfWeekService dayOfWeekService;

    public Schedule mapToEntity(DailyScheduleDto schedule) {
        if (schedule == null) {
            return null;
        }
        Schedule newSchedule = new Schedule();
        newSchedule.setId(schedule.getId());

        dayOfWeekService.checkDayOfWeekExists(schedule.getDayOfWeekId());
        DayOfWeek dayOfWeek = new DayOfWeek();
        dayOfWeek.setId(schedule.getDayOfWeekId());
        newSchedule.setDayOfWeek(dayOfWeek);

        newSchedule.setStartTime(schedule.getStartTime());
        newSchedule.setEndTime(schedule.getEndTime());
        newSchedule.setIsAvailable(true);
        return newSchedule;
    }

    public DailyScheduleDto mapToDto(Schedule schedule) {
        if (schedule == null) {
            return null;
        }
        return DailyScheduleDto.builder()
                .id(schedule.getId())
                .dayOfWeekId(schedule.getDayOfWeek() != null ? schedule.getDayOfWeek().getId() : null)
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .isAvailable(schedule.getIsAvailable())
                .build();
    }
}
