package kg.attractor.bookingsaas.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kg.attractor.bookingsaas.dto.DailyScheduleDto;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class DurationAtLeastOneHourValidator implements ConstraintValidator<DurationAtLeastOneHour, DailyScheduleDto> {

    @Override
    public boolean isValid(DailyScheduleDto dto, ConstraintValidatorContext context) {
        if (dto == null || dto.getStartTime() == null || dto.getEndTime() == null) {
            return false;
        }
        Duration duration = Duration.between(dto.getStartTime(), dto.getEndTime());
        return !duration.isNegative() && duration.toHours() >= 1;
    }
}