package kg.attractor.bookingsaas.dto.mapper.impl;

import kg.attractor.bookingsaas.dto.ServiceDto;
import kg.attractor.bookingsaas.models.Business;
import kg.attractor.bookingsaas.projection.ServiceInfo;
import kg.attractor.bookingsaas.projection.UserBusinessServiceProjection;
import org.springframework.stereotype.Service;

@Service
public class ServiceMapper {

    public ServiceDto mapToDto(kg.attractor.bookingsaas.models.Service service) {
        return ServiceDto.builder()
                .id(service.getId())
                .serviceName(service.getServiceName())
                .businessId(service.getBusiness() != null ? service.getBusiness().getId() : null)
                .price(service.getPrice())
                .durationInMinutes(service.getDurationInMinutes())
                .build();
    }

    public ServiceDto mapToDto(ServiceInfo service) {
        return ServiceDto.builder()
                .id(service.getId())
                .serviceName(service.getServiceName())
                .businessId(service.getBusiness() != null ? service.getBusiness().getId() : null)
                .price(service.getPrice())
                .durationInMinutes(service.getDurationInMinutes())
                .build();
    }

    public kg.attractor.bookingsaas.models.Service mapToModel(ServiceDto dto) {
        if (dto == null) {
            return null;
        }
        kg.attractor.bookingsaas.models.Service service = new kg.attractor.bookingsaas.models.Service();
        service.setId(dto.getId());
        service.setServiceName(dto.getServiceName());
        service.setPrice(dto.getPrice());

        Business business = new Business();
        business.setId(dto.getBusinessId());
        service.setBusiness(business);
        service.setDurationInMinutes(dto.getDurationInMinutes());
        return service;
    }

    public void updateModelFromDto(ServiceDto dto, kg.attractor.bookingsaas.models.Service existingService) {
        if (dto == null || existingService == null) {
            return;
        }
        if (dto.getServiceName() != null) {
            existingService.setServiceName(dto.getServiceName());
        }
        if (dto.getPrice() != null) {
            existingService.setPrice(dto.getPrice());
        }
        if (dto.getDurationInMinutes() != null) {
            existingService.setDurationInMinutes(dto.getDurationInMinutes());
        }
    }
}
