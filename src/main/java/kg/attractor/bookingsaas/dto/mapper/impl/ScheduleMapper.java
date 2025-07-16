package kg.attractor.bookingsaas.dto.mapper.impl;

import kg.attractor.bookingsaas.dto.DailyScheduleDto;
import kg.attractor.bookingsaas.models.DayOfWeek;
import kg.attractor.bookingsaas.models.Schedule;
import kg.attractor.bookingsaas.models.ScheduleSettings;
import kg.attractor.bookingsaas.service.DayOfWeekService;
import kg.attractor.bookingsaas.service.DayOfWeekValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleMapper {
    private final DayOfWeekValidator dayOfWeekService;
    private final ScheduleSettingsMapper scheduleSettingsMapper;

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

        kg.attractor.bookingsaas.models.Service service = new kg.attractor.bookingsaas.models.Service();
        service.setId(schedule.getServiceId());
        newSchedule.setService(service);

        newSchedule.setStartTime(schedule.getStartTime());
        newSchedule.setEndTime(schedule.getEndTime());
        newSchedule.setIsAvailable(true);
        newSchedule.setMaxBookingSize(schedule.getMaxBookingSize());

        ScheduleSettings scheduleSettings = scheduleSettingsMapper.mapToModel(schedule.getScheduleSettingsDto());
        scheduleSettings.setSchedule(newSchedule);
        newSchedule.setScheduleSettings(scheduleSettings);
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
                .maxBookingSize(schedule.getMaxBookingSize())
                .serviceId(schedule.getService() != null ? schedule.getService().getId() : null)
                .scheduleSettingsDto(scheduleSettingsMapper.mapToDto(schedule.getScheduleSettings()))
                .build();
    }

    public void updateFrom(DailyScheduleDto updated, Schedule schedule) {
        if (updated.getDayOfWeekId() != null) {
            dayOfWeekService.checkDayOfWeekExists(updated.getDayOfWeekId());
            DayOfWeek dayOfWeek = new DayOfWeek();
            dayOfWeek.setId(updated.getDayOfWeekId());
            schedule.setDayOfWeek(dayOfWeek);
        }
        if (updated.getServiceId() != null) {
            kg.attractor.bookingsaas.models.Service service = new kg.attractor.bookingsaas.models.Service();
            service.setId(updated.getServiceId());
            schedule.setService(service);
        }
        if (updated.getStartTime() != null) {
            schedule.setStartTime(updated.getStartTime());
        }
        if (updated.getEndTime() != null) {
            schedule.setEndTime(updated.getEndTime());
        }
        if (updated.getIsAvailable() != null) {
            schedule.setIsAvailable(updated.getIsAvailable());
        }
        if (updated.getMaxBookingSize() != null) {
            schedule.setMaxBookingSize(updated.getMaxBookingSize());
        }
        if (updated.getScheduleSettingsDto() != null) {
            scheduleSettingsMapper.updateFrom(updated.getScheduleSettingsDto(), schedule.getScheduleSettings());
        }
    }
}
