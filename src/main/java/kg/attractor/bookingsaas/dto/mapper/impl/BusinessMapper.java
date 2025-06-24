package kg.attractor.bookingsaas.dto.mapper.impl;

import kg.attractor.bookingsaas.dto.BusinessDto;
import kg.attractor.bookingsaas.dto.mapper.OutputUserMapper;
import kg.attractor.bookingsaas.models.Business;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

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
}
