package kg.attractor.bookingsaas.service.impl;

import kg.attractor.bookingsaas.dto.UserBusinessServiceBookDto;
import kg.attractor.bookingsaas.service.BusinessService;
import kg.attractor.bookingsaas.service.ServiceService;
import kg.attractor.bookingsaas.service.UserBusinessServiceBookService;
import kg.attractor.bookingsaas.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserBusinessServiceBookServiceImpl implements UserBusinessServiceBookService {
    private final BusinessService businessService;
    private final UserService userService;
    private final ServiceService serviceService;

    public UserBusinessServiceBookDto getUserBusinessServiceBook(Long businessId) {
        var businessDto = businessService.getBusinessById(businessId);
        var users = userService.findClientsByBusinessId(businessId);
        users.stream()
                .map(user -> UserBusinessServiceBookDto.builder()
                        .businessDto(businessDto)
                        .user(user)
                        .services(serviceService)
                        .build())
                .toList();
    }
}
