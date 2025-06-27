package kg.attractor.bookingsaas.service.impl;

import kg.attractor.bookingsaas.dto.PageHolder;
import kg.attractor.bookingsaas.dto.ServiceDto;
import kg.attractor.bookingsaas.dto.mapper.impl.PageHolderWrapper;
import kg.attractor.bookingsaas.dto.mapper.impl.ServiceMapper;
import kg.attractor.bookingsaas.models.Service;
import kg.attractor.bookingsaas.repository.ServiceRepository;
import kg.attractor.bookingsaas.service.AuthorizedUserService;
import kg.attractor.bookingsaas.service.BusinessService;
import kg.attractor.bookingsaas.service.ServiceService;
import kg.attractor.bookingsaas.service.ServiceValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService, ServiceValidator {
    private final ServiceRepository serviceRepository;
    private final BusinessService businessService;
    private final ServiceMapper serviceMapper;
    private final PageHolderWrapper pageHolderWrapper;
    private final AuthorizedUserService authorizedUserService;

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public ServiceDto createService(ServiceDto dto) {
        businessService.isBusinessExistById(dto.getBusinessId());
        Service service = serviceMapper.mapToModel(dto);
        return serviceMapper.mapToDto(serviceRepository.save(service));
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public ServiceDto updateService(ServiceDto dto) {
        businessService.isBusinessExistById(dto.getBusinessId());
        Service existingService = serviceRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Service not found"));

        isServiceBelongsToAuthUser(existingService);
        serviceMapper.updateModelFromDto(dto, existingService);
        return serviceMapper.mapToDto(serviceRepository.save(existingService));
    }

    private void isServiceBelongsToAuthUser(Service existingService) {
        Long businessOwnerId = existingService.getBusiness().getUser().getId();
        Long currentUserId = authorizedUserService.getAuthorizedUserId();
        if (!businessOwnerId.equals(currentUserId)) {
            throw new SecurityException("You do not have permission to delete this service");
        }
    }

    @Override
    public ServiceDto deleteServiceById(Long serviceId) {
        var service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new NoSuchElementException("Service not found " + serviceId));

        isServiceBelongsToAuthUser(service);
        serviceRepository.delete(service);
        return serviceMapper.mapToDto(service);
    }

    @Override
    public PageHolder<ServiceDto> findAllServicesByBusinessTitle(String businessTitle, int page, int size) {
        businessService.checkIfBusinessExistByTitle(businessTitle);
        Pageable pageable = PageRequest.of(page, size, Sort.by("serviceName").ascending());
        Page<ServiceDto> pageServices = serviceRepository.findAllByBusinessByTitle(businessTitle, pageable)
                .map(serviceMapper::mapToDto);
        return pageHolderWrapper.wrapPageHolder(pageServices);
    }

    @Override
    public ServiceDto findServiceById(Long serviceId) {
        return serviceRepository.findById(serviceId)
                .map(serviceMapper::mapToDto)
                .orElseThrow(() -> new NoSuchElementException("Service not found with id: " + serviceId));
    }

    @Override
    public void checkIfServiceExistsById(Long serviceId) {
        if (!serviceRepository.existsById(serviceId))
            throw new NoSuchElementException("Service not found with id: " + serviceId);
    }
}
