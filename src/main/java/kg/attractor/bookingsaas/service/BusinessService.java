package kg.attractor.bookingsaas.service;

import kg.attractor.bookingsaas.dto.BusinessDto;

public interface BusinessService {
    void isBusinessExistById(Long id);

    BusinessDto getBusinessById(Long id);
}
