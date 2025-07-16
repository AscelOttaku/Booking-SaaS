package kg.attractor.bookingsaas.annotations.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kg.attractor.bookingsaas.annotations.UniqueUserEmail;
import kg.attractor.bookingsaas.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueUserEmail, String> {
    private final UserService userService;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {

        if (email.contains("@gmail.com") && email.contains(".")) {
            boolean emailExist = userService.isUserEmailIsUnique(email);
            log.info("Email exist: {}", emailExist);
            return emailExist;
        }

        return false;
    }
}
