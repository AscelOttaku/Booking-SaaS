package kg.attractor.bookingsaas.annotations.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kg.attractor.bookingsaas.dto.HolidayDto;
import kg.attractor.bookingsaas.service.HolidaysService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniqueHolidayNameValidator implements ConstraintValidator<kg.attractor.bookingsaas.annotations.UniqueHolidayName, HolidayDto> {
    private final HolidaysService holidaysService;

    @Override
    public boolean isValid(HolidayDto holidayDto, ConstraintValidatorContext context) {
        String value = holidayDto.getName();
        if (value == null || value.isBlank()) {
            return false;
        }

        if (holidayDto.getDate() == null)
            return false;

        if (!holidaysService.existsByName(value))
            return holidaysService.getHolidaysByYearFromDb(holidayDto.getDate().getYear())
                    .stream()
                    .noneMatch(existingHoliday -> existingHoliday.getName().equalsIgnoreCase(value));
        return false;
    }
}