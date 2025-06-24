package kg.attractor.bookingsaas.service;

import kg.attractor.bookingsaas.dto.ServiceDto;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface ServiceService {
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    ServiceDto createService(ServiceDto dto);
}
