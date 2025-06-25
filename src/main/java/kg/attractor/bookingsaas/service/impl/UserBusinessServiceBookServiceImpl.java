package kg.attractor.bookingsaas.service.impl;

import kg.attractor.bookingsaas.dto.BusinessDto;
import kg.attractor.bookingsaas.dto.ServiceDto;
import kg.attractor.bookingsaas.dto.UserBusinessKey;
import kg.attractor.bookingsaas.dto.UserBusinessServiceBookDto;
import kg.attractor.bookingsaas.dto.mapper.OutputUserMapper;
import kg.attractor.bookingsaas.dto.mapper.impl.BusinessMapper;
import kg.attractor.bookingsaas.dto.mapper.impl.ServiceMapper;
import kg.attractor.bookingsaas.projection.UserBusinessServiceProjection;
import kg.attractor.bookingsaas.repository.BusinessRepository;
import kg.attractor.bookingsaas.service.UserBusinessServiceBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserBusinessServiceBookServiceImpl implements UserBusinessServiceBookService {
    private final BusinessRepository businessRepository;
    private final OutputUserMapper outputUserMapper;
    private final BusinessMapper businessMapper;
    private final ServiceMapper serviceMapper;

    @Override
    public List<UserBusinessServiceBookDto> getUserBusinessServiceBook(Long businessId) {
        if (businessId == null) {
            throw new IllegalArgumentException("Business ID cannot be null");
        }

        return businessRepository.getUserBusinessServiceBookByBusinessId(businessId)
                .stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.groupingBy(
                                this::mapToUserBusinessKey,
                                Collectors.mapping(
                                        projection -> serviceMapper.mapToDto(projection.getServices()),
                                        Collectors.toList()
                                )
                        ),
                        this::mapToUserBusinessServiceBookDtos
                ));
    }

    private UserBusinessKey mapToUserBusinessKey(UserBusinessServiceProjection projection) {
        return UserBusinessKey.builder()
                .user(outputUserMapper.mapToDto(projection.getUser()))
                .businessDto(businessMapper.toDto(projection.getBusiness()))
                .build();
    }

    private List<UserBusinessServiceBookDto> mapToUserBusinessServiceBookDtos(
            Map<UserBusinessKey, List<ServiceDto>> userServicesMap) {
        return userServicesMap.entrySet().stream()
                .map(entry -> UserBusinessServiceBookDto.builder()
                        .user(entry.getKey().getUser())
                        .businessDto(entry.getKey().getBusinessDto())
                        .services(entry.getValue())
                        .build())
                .toList();
    }

    public BusinessDto findBusinessById(Long id) {
        return businessMapper.toDto(businessRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Business not found by id " + id)));
    }
}
