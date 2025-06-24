package kg.attractor.bookingsaas.service.impl;

import kg.attractor.bookingsaas.dto.ServiceDto;
import kg.attractor.bookingsaas.dto.impl.ServiceMapper;
import kg.attractor.bookingsaas.models.Service;
import kg.attractor.bookingsaas.repository.ServiceRepository;
import kg.attractor.bookingsaas.service.BusinessService;
import kg.attractor.bookingsaas.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService {
    private final ServiceRepository serviceRepository;
    private final BusinessService businessService;
    private final ServiceMapper serviceMapper;


    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public ServiceDto createService(ServiceDto dto) {
        businessService.isBusinessExistById(dto.getBusinessId());
        Service service = serviceMapper.mapToModel(dto);
        service.getBooks().forEach(book -> book.setServices(service));
        return serviceMapper.mapToDto(serviceRepository.save(service));
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public ServiceDto updateService(ServiceDto dto) {
        businessService.isBusinessExistById(dto.getBusinessId());
        Service existingService = serviceRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Service not found"));
        serviceMapper.updateModelFromDto(dto, existingService);
        existingService.getBooks().forEach(book -> book.setServices(existingService));
        return serviceMapper.mapToDto(serviceRepository.save(existingService));
    }
}
