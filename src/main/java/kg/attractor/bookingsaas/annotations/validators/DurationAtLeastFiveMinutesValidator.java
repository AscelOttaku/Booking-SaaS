package kg.attractor.bookingsaas.annotations.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kg.attractor.bookingsaas.annotations.DurationAtLeastFiveMinutes;
import kg.attractor.bookingsaas.dto.BreakPeriodDto;
import kg.attractor.bookingsaas.service.BreakPeriodService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class DurationAtLeastFiveMinutesValidator implements ConstraintValidator<DurationAtLeastFiveMinutes, BreakPeriodDto> {
    private final BreakPeriodService breakPeriodService;

    @Override
    public boolean isValid(BreakPeriodDto breakPeriodDto, ConstraintValidatorContext context) {
        if (breakPeriodDto == null) return true;
        LocalTime start = breakPeriodDto.getStart();
        LocalTime end = breakPeriodDto.getEnd();

        if (start == null && end == null)
            return true;

        if (start == null ^ end == null) {
            var breakPeriod = breakPeriodService.findBreakPeriodById(breakPeriodDto.getId());
            if (breakPeriod.isPresent()) {
                if (start == null) start = breakPeriod.get().getStart();
                if (end == null) end = breakPeriod.get().getEnd();
            }
        }

        if (start == null || end == null) {
            return false;
        }
        return java.time.Duration.between(start, end).toMinutes() >= 5;
    }
}
