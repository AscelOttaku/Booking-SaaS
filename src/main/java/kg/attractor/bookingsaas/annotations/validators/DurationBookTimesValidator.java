package kg.attractor.bookingsaas.annotations.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kg.attractor.bookingsaas.annotations.DurationBookTimes;
import kg.attractor.bookingsaas.dto.booked.BookDto;
import org.springframework.stereotype.Service;

@Service
public class DurationBookTimesValidator implements ConstraintValidator<DurationBookTimes, BookDto> {
    private long durationInSeconds;

    @Override
    public void initialize(DurationBookTimes constraintAnnotation) {
        this.durationInSeconds = constraintAnnotation.durationInSeconds();
    }

    @Override
    public boolean isValid(BookDto createBookDto, ConstraintValidatorContext constraintValidatorContext) {
        if (createBookDto == null) {
            return true;
        }
        if (createBookDto.getStartedAt() == null || createBookDto.getFinishedAt() == null) {
            return false;
        }
        if (createBookDto.getStartedAt().isAfter(createBookDto.getFinishedAt())) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            String message = "startedAt must be before finishedAt";
            constraintValidatorContext.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode("startedAt")
                    .addConstraintViolation();

            constraintValidatorContext.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode("finishedAt")
                    .addConstraintViolation();
            return false;
        }
        long actualDuration = java.time.Duration.between(
                createBookDto.getStartedAt(),
                createBookDto.getFinishedAt()
        ).getSeconds();
        if (actualDuration < durationInSeconds) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            String message = "Duration between startedAt and finishedAt should be at least " + durationInSeconds + " seconds";
            constraintValidatorContext.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode("startedAt")
                    .addConstraintViolation();

            constraintValidatorContext.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode("finishedAt")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
