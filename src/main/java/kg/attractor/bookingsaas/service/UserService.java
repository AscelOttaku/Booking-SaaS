package kg.attractor.bookingsaas.service;

import kg.attractor.bookingsaas.dto.PageHolder;
import kg.attractor.bookingsaas.dto.user.OutputUserDto;
import kg.attractor.bookingsaas.dto.user.UpdateUserDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.util.List;

public interface UserService {
    boolean isUserEmailIsUnique(String email);

    boolean isUserPhoneNumberUnique(String phoneNumber);

    UserDetails getAuthUser();

    UpdateUserDto updateUser(UpdateUserDto updateUserDto) throws IOException;

    List<OutputUserDto> findClientsByBusinessId(Long businessId);

    PageHolder<OutputUserDto> getUsersByBusinessTitle(String businessTitle, int page, int size);

    PageHolder<OutputUserDto> findUsersByServiceId(Long serviceId, int page, int szie);
}
