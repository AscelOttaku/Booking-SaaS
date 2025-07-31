package kg.attractor.bookingsaas.dto.mapper.impl;

import kg.attractor.bookingsaas.dto.BusinessDto;
import kg.attractor.bookingsaas.dto.mapper.OutputUserMapper;
import kg.attractor.bookingsaas.models.Business;
import kg.attractor.bookingsaas.models.Service;
import kg.attractor.bookingsaas.projection.BusinessInfo;
import kg.attractor.bookingsaas.util.Util;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static kg.attractor.bookingsaas.util.Util.distinctByKey;

@Component
public class BusinessMapper {

    private final ServiceMapper serviceMapper;
    private final OutputUserMapper userMapper;

    public BusinessMapper(ServiceMapper serviceMapper, OutputUserMapper userMapper) {
        this.serviceMapper = serviceMapper;
        this.userMapper = userMapper;
    }

    public BusinessDto toDto(Business entity) {
        if (entity == null) return null;

        BusinessDto dto = new BusinessDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        dto.setUser(userMapper.mapToDto(entity.getUser()));

        if (entity.getServices() != null) {
            dto.setServices(
                    entity.getServices().stream()
                            .map(serviceMapper::mapToDto)
                            .collect(Collectors.toList())
            );
        }
        return dto;
    }

    public BusinessDto toDto(BusinessInfo businessInfo) {
        if (isNull(businessInfo)) return null;

        BusinessDto dto = new BusinessDto();
        dto.setId(businessInfo.getId());
        dto.setTitle(businessInfo.getTitle());
        dto.setDescription(businessInfo.getDescription());
        dto.setCreatedAt(businessInfo.getCreatedAt());
        dto.setUpdatedAt(businessInfo.getUpdatedAt());

        if (dto.getServices() != null) {
            dto.setServices(businessInfo.getServices().stream()
                    .map(serviceMapper::mapToDto)
                    .toList());
        }
        if (businessInfo.getUser() != null) {
            dto.setUser(userMapper.mapToDto(businessInfo.getUser()));
        }
        return dto;
    }

    private static boolean isNull(BusinessInfo businessInfo) {
        return businessInfo == null;
    }

    public Business toEntity(BusinessDto dto) {
        if (dto == null) return null;

        Business entity = new Business();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());

        entity.setUser(userMapper.mapToEntity(dto.getUser()));

        if (dto.getServices() != null) {
            entity.setServices(
                    dto.getServices().stream()
                            .map(serviceMapper::mapToModel)
                            .collect(Collectors.toList())
            );
        }

        return entity;
    }

    public void updateEntityFromDto(BusinessDto businessDto, Business existingBusiness) {
        if (businessDto == null || existingBusiness == null) {
            return;
        }
        existingBusiness.setTitle(businessDto.getTitle());
        existingBusiness.setDescription(businessDto.getDescription());

        Function<Service, String> functionalInterface = service ->
                service.getServiceName() + " " + service.getPrice();

        var mergedUniqueService = Stream.concat(
                existingBusiness.getServices().stream(),
                businessDto.getServices().stream().map(serviceMapper::mapToModel)
        )
                .filter(distinctByKey(functionalInterface))
                .toList();
        existingBusiness.setServices(mergedUniqueService);
    }
}
