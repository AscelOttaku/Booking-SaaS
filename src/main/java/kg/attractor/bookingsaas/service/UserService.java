package kg.attractor.bookingsaas.service;

import kg.attractor.bookingsaas.dto.UpdateUserDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;

public interface UserService {
    boolean isUserEmailIsUnique(String email);

    boolean isUserPhoneNumberUnique(String phoneNumber);

    UserDetails getAuthUser();

    UpdateUserDto updateUser(UpdateUserDto updateUserDto) throws IOException;
}
