package kg.attractor.bookingsaas.api;

import kg.attractor.bookingsaas.dto.DailyScheduleDto;
import kg.attractor.bookingsaas.dto.WeeklyScheduleDto;
import kg.attractor.bookingsaas.markers.OnCreate;
import kg.attractor.bookingsaas.markers.OnUpdate;
import kg.attractor.bookingsaas.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class ScheduleApi {
    private final ScheduleService scheduleService;

    @PostMapping("weekly")
    @ResponseStatus(HttpStatus.OK)
    public WeeklyScheduleDto createWeeklySchedule(@RequestBody @Validated(value = OnCreate.class) WeeklyScheduleDto weeklyScheduleDto) {
        return scheduleService.createWeeklySchedule(weeklyScheduleDto);
    }

    @PostMapping("daily")
    @ResponseStatus(HttpStatus.OK)
    public DailyScheduleDto createDailySchedule(@RequestBody @Validated(value = OnCreate.class) DailyScheduleDto dailyScheduleDto) {
        return scheduleService.createDailySchedule(dailyScheduleDto);
    }

    @PutMapping("daily")
    @ResponseStatus(HttpStatus.OK)
    public DailyScheduleDto updateDailySchedule(@RequestBody @Validated(value = OnUpdate.class) DailyScheduleDto dailyScheduleDto) {
        return scheduleService.updateDailySchedule(dailyScheduleDto);
    }

    @PutMapping("weekly")
    @ResponseStatus(HttpStatus.OK)
    public WeeklyScheduleDto updateDailySchedule(@RequestBody @Validated(value = OnUpdate.class) WeeklyScheduleDto weeklyScheduleDto) {
        return scheduleService.updateWeeklySchedule(weeklyScheduleDto);
    }
}
