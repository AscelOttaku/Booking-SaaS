package kg.attractor.bookingsaas.annotations.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kg.attractor.bookingsaas.annotations.DurationBookTimes;
import kg.attractor.bookingsaas.dto.BookDto;

public class DurationBookTimesValidator implements ConstraintValidator<DurationBookTimes, BookDto> {
    private long durationInSeconds;

    @Override
    public void initialize(DurationBookTimes constraintAnnotation) {
        this.durationInSeconds = constraintAnnotation.durationInSeconds();
    }

    @Override
    public boolean isValid(BookDto bookDto, ConstraintValidatorContext constraintValidatorContext) {
        if (bookDto == null) {
            return true;
        }
        if (bookDto.getStartedAt() == null || bookDto.getFinishedAt() == null) {
            return false;
        }
        long actualDuration = java.time.Duration.between(
                bookDto.getStartedAt(),
                bookDto.getFinishedAt()
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
