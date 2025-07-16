package kg.attractor.bookingsaas.annotations.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kg.attractor.bookingsaas.annotations.DurationAtLeastOneHour;
import kg.attractor.bookingsaas.dto.DailyScheduleDto;
import kg.attractor.bookingsaas.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class DurationAtLeastOneHourValidator implements ConstraintValidator<DurationAtLeastOneHour, DailyScheduleDto> {
    private final ScheduleService scheduleService;

    @Override
    public boolean isValid(DailyScheduleDto dto, ConstraintValidatorContext context) {
        if (dto == null) {
            return false;
        }

        java.time.LocalTime start = dto.getStartTime();
        java.time.LocalTime end = dto.getEndTime();

        if (start == null || end == null) {
            var scheduleTime = scheduleService.findScheduleTimeById(dto.getId());
            if (start == null && scheduleTime.getStartTime() != null) {
                start = scheduleTime.getStartTime();
            }
            if (end == null && scheduleTime.getEndTime() != null) {
                end = scheduleTime.getEndTime();
            }
        }

        return isTimeCorrectFormat(start, end);
    }

    private static boolean isTimeCorrectFormat(java.time.LocalTime start, java.time.LocalTime end) {
        if (start == null || end == null) {
            return false;
        }
        Duration duration = Duration.between(start, end);
        return !duration.isNegative() && duration.toHours() >= 1;
    }
}