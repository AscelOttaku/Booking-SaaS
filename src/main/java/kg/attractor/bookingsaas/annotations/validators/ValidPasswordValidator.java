package kg.attractor.bookingsaas.annotations.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kg.attractor.bookingsaas.annotations.ValidUserPassword;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidPasswordValidator implements ConstraintValidator<ValidUserPassword, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        constraintValidatorContext.disableDefaultConstraintViolation();

        if (password == null || password.isBlank()) {
            constraintValidatorContext.buildConstraintViolationWithTemplate(
                            "password is null or blank"
                    )
                    .addConstraintViolation();
            return false;
        }

        if (!password.matches("^(?=.*[A-Z])(?=.*\\d).+$")) {
            constraintValidatorContext.buildConstraintViolationWithTemplate(
                            "Password must contain at least one uppercase letter and one digit"
                    )
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
