package kg.attractor.bookingsaas.service;

import kg.attractor.bookingsaas.models.Service;

import java.util.List;

public interface ServiceValidator {
    void checkIfServiceExistsById(Long serviceId);

    void checkServiceBelongsToAuthUser(Long serviceId);

    void validateServices(List<Service> services);
}
