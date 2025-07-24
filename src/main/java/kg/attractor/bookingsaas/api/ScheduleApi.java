package kg.attractor.bookingsaas.api;

import kg.attractor.bookingsaas.dto.DailyScheduleDto;
import kg.attractor.bookingsaas.dto.ScheduleAvailableSlots;
import kg.attractor.bookingsaas.dto.WeeklyScheduleDto;
import kg.attractor.bookingsaas.markers.OnCreate;
import kg.attractor.bookingsaas.markers.OnUpdate;
import kg.attractor.bookingsaas.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class ScheduleApi {
    private final ScheduleService scheduleService;

    @PreAuthorize("hasAnyAuthority('BUSINESS_OWNER')")
    @PostMapping("weekly")
    @ResponseStatus(HttpStatus.OK)
    public WeeklyScheduleDto createWeeklySchedule(@RequestBody @Validated(value = OnCreate.class) WeeklyScheduleDto weeklyScheduleDto) {
        return scheduleService.createWeeklySchedule(weeklyScheduleDto);
    }

    @PreAuthorize("hasAnyAuthority('BUSINESS_OWNER')")
    @PostMapping("daily")
    @ResponseStatus(HttpStatus.OK)
    public DailyScheduleDto createDailySchedule(@RequestBody @Validated(value = OnCreate.class) DailyScheduleDto dailyScheduleDto) {
        return scheduleService.createDailySchedule(dailyScheduleDto);
    }

    @PreAuthorize("hasAnyAuthority('BUSINESS_OWNER')")
    @PutMapping("daily")
    @ResponseStatus(HttpStatus.OK)
    public DailyScheduleDto updateDailySchedule(@RequestBody @Validated(value = OnUpdate.class) DailyScheduleDto dailyScheduleDto) {
        return scheduleService.updateDailySchedule(dailyScheduleDto);
    }

    @PreAuthorize("hasAnyAuthority('BUSINESS_OWNER')")
    @PutMapping("weekly")
    @ResponseStatus(HttpStatus.OK)
    public WeeklyScheduleDto updateDailySchedule(@RequestBody @Validated(value = OnUpdate.class) WeeklyScheduleDto weeklyScheduleDto) {
        return scheduleService.updateWeeklySchedule(weeklyScheduleDto);
    }

    @PreAuthorize("hasAnyAuthority('BUSINESS_OWNER')")
    @DeleteMapping("daily/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDailySchedule(@PathVariable Long id) {
        scheduleService.deleteDailyScheduleById(id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("available-slots/{scheduleId}")
    @ResponseStatus(HttpStatus.OK)
    public ScheduleAvailableSlots getAvailableSlots(
            @PathVariable Long scheduleId, @RequestParam String date
    ) {
        return scheduleService.findScheduleAvailableSlotsForBooking(scheduleId, date);
    }
}
