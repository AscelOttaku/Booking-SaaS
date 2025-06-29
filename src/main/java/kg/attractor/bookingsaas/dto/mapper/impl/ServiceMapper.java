package kg.attractor.bookingsaas.dto.mapper.impl;

import kg.attractor.bookingsaas.dto.ServiceDto;
import kg.attractor.bookingsaas.models.Business;
import kg.attractor.bookingsaas.projection.UserBusinessServiceProjection;
import org.springframework.stereotype.Service;

@Service
public class ServiceMapper {

    public ServiceDto mapToDto(kg.attractor.bookingsaas.models.Service service) {
        return ServiceDto.builder()
                .id(service.getId())
                .serviceName(service.getServiceName())
                .businessId(service.getBusiness() != null ? service.getBusiness().getId() : null)
                .duration(service.getDuration())
                .price(service.getPrice())
                .build();
    }

    public ServiceDto mapToDto(UserBusinessServiceProjection.ServiceInfo service) {
        return ServiceDto.builder()
                .id(service.getId())
                .serviceName(service.getServiceName())
                .businessId(service.getBusiness() != null ? service.getBusiness().getId() : null)
                .duration(service.getDuration())
                .price(service.getPrice())
                .build();
    }

    public kg.attractor.bookingsaas.models.Service mapToModel(ServiceDto dto) {
        if (dto == null) {
            return null;
        }
        kg.attractor.bookingsaas.models.Service service = new kg.attractor.bookingsaas.models.Service();
        service.setId(dto.getId());
        service.setServiceName(dto.getServiceName());
        service.setDuration(dto.getDuration());
        service.setPrice(dto.getPrice());

        Business business = new Business();
        business.setId(dto.getBusinessId());
        service.setBusiness(business);
        return service;
    }

    public void updateModelFromDto(ServiceDto dto, kg.attractor.bookingsaas.models.Service existingService) {
        if (dto == null || existingService == null) {
            return;
        }
        if (dto.getServiceName() != null) {
            existingService.setServiceName(dto.getServiceName());
        }
        if (dto.getDuration() != null) {
            existingService.setDuration(dto.getDuration());
        }
        if (dto.getPrice() != null) {
            existingService.setPrice(dto.getPrice());
        }
    }
}
