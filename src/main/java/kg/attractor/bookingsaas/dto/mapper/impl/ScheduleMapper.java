package kg.attractor.bookingsaas.dto.mapper.impl;

import kg.attractor.bookingsaas.dto.ScheduleDto;
import kg.attractor.bookingsaas.models.DayOfWeekEntity;
import kg.attractor.bookingsaas.models.Schedule;
import kg.attractor.bookingsaas.service.DayOfWeekService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleMapper {
    private final DayOfWeekService dayOfWeekService;

    public Schedule mapToEntity(ScheduleDto schedule) {
        if (schedule == null) {
            return null;
        }
        Schedule newSchedule = new Schedule();
        newSchedule.setId(schedule.getId());

        dayOfWeekService.checkDayOfWeekExists(schedule.getId());
        DayOfWeekEntity dayOfWeekEntity = new DayOfWeekEntity();
        dayOfWeekEntity.setId(schedule.getDayOfWeekId());
        newSchedule.setDayOfWeek(dayOfWeekEntity);

        newSchedule.setStartTime(schedule.getStartTime());
        newSchedule.setEndTime(schedule.getEndTime());
        return newSchedule;
    }

    public ScheduleDto mapToDto(Schedule schedule) {
        if (schedule == null) {
            return null;
        }
        return ScheduleDto.builder()
                .id(schedule.getId())
                .dayOfWeekId(schedule.getDayOfWeek() != null ? schedule.getDayOfWeek().getId() : null)
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .build();
    }
}
