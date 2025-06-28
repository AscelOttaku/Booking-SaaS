package kg.attractor.bookingsaas.dto.mapper.impl;

import kg.attractor.bookingsaas.dto.ScheduleSettingsDto;
import kg.attractor.bookingsaas.dto.mapper.BreakPeriodMapper;
import kg.attractor.bookingsaas.models.ScheduleSettings;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class ScheduleSettingsMapper {
    private final BreakPeriodMapper breakPeriodMapper;

    public ScheduleSettingsDto mapToDto(ScheduleSettings scheduleSettings) {
        if (scheduleSettings == null) {
            return null;
        }
        ScheduleSettingsDto dto = new ScheduleSettingsDto();
        dto.setId(scheduleSettings.getId());
        dto.setBreakBetweenBookings(scheduleSettings.getBreakBetweenBookings());
        dto.setBreakPeriodDto(
                scheduleSettings.getBreakPeriods() == null ? Collections.emptyList() :
                        scheduleSettings.getBreakPeriods().stream()
                                .map(breakPeriod -> {
                                    var dtoItem = breakPeriodMapper.toDto(breakPeriod);
                                    dtoItem.setScheduleSettingsId(scheduleSettings.getId());
                                    return dtoItem;
                                })
                                .toList()
        );
        return dto;
    }

    public ScheduleSettings mapToModel(ScheduleSettingsDto scheduleSettings) {
        if (scheduleSettings == null) {
            return null;
        }
        ScheduleSettings model = new ScheduleSettings();
        model.setId(scheduleSettings.getId());
        model.setBreakBetweenBookings(scheduleSettings.getBreakBetweenBookings());
        model.setBreakPeriods(
                scheduleSettings.getBreakPeriodDto() == null ? Collections.emptyList() :
                        scheduleSettings.getBreakPeriodDto().stream()
                                .map(breakPeriodDto -> {
                                    var breakPeriod = breakPeriodMapper.toEntity(breakPeriodDto);
                                    breakPeriod.setSettings(model);
                                    return breakPeriod;
                                })
                                .toList()
        );
        return model;
    }

    public void updateFrom(ScheduleSettingsDto scheduleSettingsDto, ScheduleSettings scheduleSettings) {
        if (scheduleSettingsDto.getBreakBetweenBookings() != null) {
            scheduleSettings.setBreakBetweenBookings(scheduleSettingsDto.getBreakBetweenBookings());
        }
        if (scheduleSettingsDto.getBreakPeriodDto() != null) {
            scheduleSettings.setBreakPeriods(
                    scheduleSettingsDto.getBreakPeriodDto().stream()
                            .map(breakPeriodDto -> {
                                var breakPeriod = breakPeriodMapper.toEntity(breakPeriodDto);
                                breakPeriod.setSettings(scheduleSettings);
                                return breakPeriod;
                            })
                            .toList()
            );
        }
    }
}
