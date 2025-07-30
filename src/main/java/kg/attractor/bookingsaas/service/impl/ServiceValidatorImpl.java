package kg.attractor.bookingsaas.service.impl;

import kg.attractor.bookingsaas.repository.ServiceRepository;
import kg.attractor.bookingsaas.service.AuthorizedUserService;
import kg.attractor.bookingsaas.service.ServiceValidator;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.internal.constraintvalidators.bv.number.bound.MaxValidatorForBigDecimal;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ServiceValidatorImpl implements ServiceValidator {
    private static final int MAX_SERVICES_PER_BUSINESS = 15;
    private final ServiceRepository serviceRepository;
    private final AuthorizedUserService authorizedUserService;
    private final MaxValidatorForBigDecimal maxValidatorForBigDecimal;

    @Override
    public void checkIfServiceExistsById(Long serviceId) {
        if (!serviceRepository.existsById(serviceId))
            throw new NoSuchElementException("Service not found with id: " + serviceId);
    }

    @Override
    public void checkServiceBelongsToAuthUser(Long serviceId) {
        var service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new NoSuchElementException("Service not found with id: " + serviceId));

        Long businessOwnerId = service.getBusiness().getUser().getId();
        if (!Objects.equals(businessOwnerId, authorizedUserService.getAuthorizedUserId()))
            throw new IllegalArgumentException("You do not have permission to access this service");
    }

    @Override
    public void validateServices(List<kg.attractor.bookingsaas.models.Service> services) {
        if (services != null && !services.isEmpty() && services.size() >= MAX_SERVICES_PER_BUSINESS) {
            throw new IllegalArgumentException("A business cannot have more than " + MAX_SERVICES_PER_BUSINESS + " services");
        }
    }
}
