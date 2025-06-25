package kg.attractor.bookingsaas.annotations.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kg.attractor.bookingsaas.annotations.BusinessUniqueTitle;
import kg.attractor.bookingsaas.service.BusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UniqueBusinessTitleValidator implements ConstraintValidator<BusinessUniqueTitle, String> {
    private final BusinessService businessService;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s == null || businessService.isBusinessTitleIsUnique(s);
    }
}
