package kg.attractor.bookingsaas.api;

import jakarta.validation.Valid;
import kg.attractor.bookingsaas.dto.DailyScheduleDto;
import kg.attractor.bookingsaas.dto.WeeklyScheduleDto;
import kg.attractor.bookingsaas.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class ScheduleApi {
    private final ScheduleService scheduleService;

    @PostMapping("weekly")
    @ResponseStatus(HttpStatus.OK)
    public WeeklyScheduleDto createWeeklySchedule(@RequestBody @Valid WeeklyScheduleDto weeklyScheduleDto) {
        return scheduleService.createWeeklySchedule(weeklyScheduleDto);
    }

    @PostMapping("daily")
    @ResponseStatus(HttpStatus.OK)
    public DailyScheduleDto createDailySchedule(@RequestBody @Valid DailyScheduleDto dailyScheduleDto) {
        return scheduleService.createDailySchedule(dailyScheduleDto);
    }
}
