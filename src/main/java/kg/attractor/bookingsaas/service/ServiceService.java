package kg.attractor.bookingsaas.service;

import kg.attractor.bookingsaas.dto.PageHolder;
import kg.attractor.bookingsaas.dto.ServiceDto;
import kg.attractor.bookingsaas.dto.booked.BookServiceDto;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ServiceService {
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    ServiceDto createService(ServiceDto dto);

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    ServiceDto updateService(ServiceDto dto);

    ServiceDto deleteServiceById(Long serviceId);

    PageHolder<ServiceDto> findAllServicesByBusinessTitle(String businessTitle, int page, int size);

    PageHolder<BookServiceDto> findServiceAndBooksByServiceId(Long serviceId, int page, int size);

    ServiceDto findServiceById(Long serviceId);

    ServiceDto findMostPopularServiceByBusinessTitle(String businessTitle);

    List<ServiceDto> findServicesSortedByPopularityByBusinessTitle(String businessTitle);
}
