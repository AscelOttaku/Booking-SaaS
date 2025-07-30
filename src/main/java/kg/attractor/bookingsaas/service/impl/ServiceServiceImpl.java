package kg.attractor.bookingsaas.service.impl;

import kg.attractor.bookingsaas.dto.PageHolder;
import kg.attractor.bookingsaas.dto.ServiceDto;
import kg.attractor.bookingsaas.dto.booked.BookServiceDto;
import kg.attractor.bookingsaas.dto.mapper.impl.BookMapper;
import kg.attractor.bookingsaas.dto.mapper.impl.PageHolderWrapper;
import kg.attractor.bookingsaas.dto.mapper.impl.ServiceMapper;
import kg.attractor.bookingsaas.models.Service;
import kg.attractor.bookingsaas.repository.ServiceRepository;
import kg.attractor.bookingsaas.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService, ServiceDurationProvider {
    private final ServiceRepository serviceRepository;
    private final BusinessValidator businessValidator;
    private final ServiceMapper serviceMapper;
    private final PageHolderWrapper pageHolderWrapper;
    private final BookMapper bookMapper;

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public ServiceDto createService(ServiceDto dto) {
        int servicesQuantityForBusiness = serviceRepository.countByBusinessId(dto.getBusinessId());
        if (servicesQuantityForBusiness >= 15) {
            throw new IllegalArgumentException("You cannot create more than 15 services for a business");
        }

        businessValidator.checkIsBusinessBelongsToAuthUser(dto.getBusinessId());
        Service service = serviceMapper.mapToModel(dto);
        return serviceMapper.mapToDto(serviceRepository.save(service));
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public ServiceDto updateService(ServiceDto dto) {
        businessValidator.checkIsBusinessBelongsToAuthUser(dto.getBusinessId());
        Service existingService = serviceRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Service not found"));

        serviceMapper.updateModelFromDto(dto, existingService);
        return serviceMapper.mapToDto(serviceRepository.save(existingService));
    }

    @Override
    public ServiceDto deleteServiceById(Long serviceId) {
        var service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new NoSuchElementException("Service not found " + serviceId));

        businessValidator.checkIsBusinessBelongsToAuthUser(service.getBusiness().getId());
        serviceRepository.delete(service);
        return serviceMapper.mapToDto(service);
    }

    @Override
    public PageHolder<ServiceDto> findAllServicesByBusinessTitle(String businessTitle, int page, int size) {
        businessValidator.checkIfBusinessExistByTitle(businessTitle);
        Pageable pageable = PageRequest.of(page, size, Sort.by("serviceName").ascending());
        Page<ServiceDto> pageServices = serviceRepository.findAllByBusinessTitle(businessTitle, pageable)
                .map(serviceMapper::mapToDto);
        return pageHolderWrapper.wrapPageHolder(pageServices);
    }

    @Override
    public PageHolder<BookServiceDto> findServiceAndBooksByServiceId(Long serviceId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        var clientsPage = serviceRepository.findClientsByServiceId(serviceId, pageable);

        var grouped = clientsPage.getContent().stream()
                .collect(Collectors.groupingBy(
                        userBook -> serviceMapper.mapToDto(userBook.getService()),
                        Collectors.mapping(userBook -> bookMapper.toDto(userBook.getBook()), Collectors.toList())
                ));

        List<BookServiceDto> bookServiceDtos = grouped.entrySet().stream()
                .map(entry -> BookServiceDto.builder()
                        .serviceDto(entry.getKey())
                        .bookDtos(entry.getValue())
                        .build())
                .toList();

        return PageHolder.<BookServiceDto>builder()
                .content(bookServiceDtos)
                .page(clientsPage.getNumber())
                .size(clientsPage.getSize())
                .totalPages(clientsPage.getTotalPages())
                .totalElements(clientsPage.getTotalElements())
                .hasNextPage(clientsPage.hasNext())
                .hasPreviousPage(clientsPage.hasPrevious())
                .build();
    }

    @Override
    public ServiceDto findServiceById(Long serviceId) {
        return serviceRepository.findById(serviceId)
                .map(serviceMapper::mapToDto)
                .orElseThrow(() -> new NoSuchElementException("Service not found with id: " + serviceId));
    }

    @Override
    public int findServiceDurationByScheduleId(Long scheduleId) {
        return serviceRepository.findServiceDurationByScheduleId((scheduleId))
                .orElseThrow(() -> new NoSuchElementException("Service not found with schedule id: " + scheduleId));
    }

    @Override
    public ServiceDto findMostPopularServiceByBusinessTitle(String businessTitle) {
        businessValidator.checkIsBusinessBelongsToAuthUser(businessTitle);
        return serviceRepository.findServicesByBusinessTitle(businessTitle)
                .stream()
                .max(Comparator.comparing(service -> service.getSchedules().stream()
                        .mapToInt(schedule -> schedule.getBooks().size())
                        .sum()
                ))
                .map(serviceMapper::mapToDto)
                .orElseThrow(() -> new NoSuchElementException("No services found for business: " + businessTitle));
    }

    @Override
    public List<ServiceDto> findServicesSortedByPopularityByBusinessTitle(String businessTitle) {
        businessValidator.checkIsBusinessBelongsToAuthUser(businessTitle);
        return serviceRepository.findServicesByBusinessTitle(businessTitle)
                .stream()
                .sorted(Comparator.comparing(service -> service.getSchedules().stream()
                        .mapToLong(schedule -> schedule.getBooks().size())
                        .sum()))
                .map(serviceMapper::mapToDto)
                .toList();
    }
}
