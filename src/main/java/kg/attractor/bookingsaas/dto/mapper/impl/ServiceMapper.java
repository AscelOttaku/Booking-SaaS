package kg.attractor.bookingsaas.dto.mapper.impl;

import kg.attractor.bookingsaas.dto.ServiceDto;
import kg.attractor.bookingsaas.projection.UserBusinessServiceProjection;
import org.springframework.stereotype.Service;

@Service
public class ServiceMapper {
    private final BookMapper bookMapper;

    public ServiceMapper(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    public ServiceDto mapToDto(kg.attractor.bookingsaas.models.Service service) {
        return ServiceDto.builder()
                .id(service.getId())
                .serviceName(service.getServiceName())
                .businessId(service.getBusiness().getId())
                .books(service.getBooks() != null ?
                        service.getBooks().stream()
                                .map(bookMapper::toDto)
                                .toList() : null)
                .businessId(service.getBusiness() != null ? service.getBusiness().getId() : null)
                .build();
    }

    public ServiceDto mapToDto(UserBusinessServiceProjection.ServiceInfo service) {
        return ServiceDto.builder()
                .id(service.getId())
                .serviceName(service.getServiceName())
                .books(service.getBooks() != null ?
                        service.getBooks().stream()
                                .map(bookMapper::toDto)
                                .toList() : null)
                .businessId(service.getBusiness() != null ? service.getBusiness().getId() : null)
                .build();
    }

    public kg.attractor.bookingsaas.models.Service mapToModel(ServiceDto dto) {
        if (dto == null) {
            return null;
        }
        kg.attractor.bookingsaas.models.Service service = new kg.attractor.bookingsaas.models.Service();
        service.setId(dto.getId());
        service.setServiceName(dto.getServiceName());
        if (dto.getBooks() != null) {
            service.setBooks(dto.getBooks().stream()
                    .map(bookMapper::toEntity)
                    .toList());
        } else {
            service.setBooks(null);
        }
        return service;
    }

    public void updateModelFromDto(ServiceDto dto, kg.attractor.bookingsaas.models.Service existingService) {
        if (dto != null && existingService != null) {
            existingService.setId(dto.getId());
            existingService.setServiceName(dto.getServiceName());
            if (dto.getBooks() != null) {
                existingService.setBooks(dto.getBooks().stream()
                        .map(bookMapper::toEntity)
                        .toList());
            } else {
                existingService.setBooks(null);
            }
        }
    }
}
