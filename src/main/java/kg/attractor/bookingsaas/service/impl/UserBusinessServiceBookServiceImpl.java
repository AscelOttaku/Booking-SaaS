package kg.attractor.bookingsaas.service.impl;

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
        Map<UserBusinessKey, List<ServiceDto>> userBusinessServiceProjections = businessRepository
                .getUserBusinessServiceBookByBusinessId(businessId)
                .stream()
                .collect(Collectors.groupingBy(
                        (UserBusinessServiceProjection userBusinessServiceProjection) ->
                                UserBusinessKey.builder()
                                        .user(outputUserMapper.mapToDto(userBusinessServiceProjection.getUser()))
                                        .businessDto(businessMapper.toDto(userBusinessServiceProjection.getBusiness()))
                                        .build(),
                        Collectors.mapping(userBusinessServiceProjection ->
                                        serviceMapper.mapToDto(userBusinessServiceProjection.getServices()),
                                Collectors.toList()
                        )));

        return userBusinessServiceProjections.entrySet().stream()
                .map(userBusinessKeyListEntry -> UserBusinessServiceBookDto
                        .builder()
                        .user(userBusinessKeyListEntry.getKey().getUser())
                        .businessDto(userBusinessKeyListEntry.getKey().getBusinessDto())
                        .services(userBusinessKeyListEntry.getValue())
                        .build())
                .toList();
    }
}
