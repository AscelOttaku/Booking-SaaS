package kg.attractor.bookingsaas.module.service.impl;

import kg.attractor.bookingsaas.dto.mapper.OutputUserMapperImpl;
import kg.attractor.bookingsaas.dto.mapper.UpdateUserMapperImpl;
import kg.attractor.bookingsaas.dto.mapper.impl.PageHolderWrapper;
import kg.attractor.bookingsaas.dto.user.UpdateUserDto;
import kg.attractor.bookingsaas.enums.RoleEnum;
import kg.attractor.bookingsaas.repository.UserRepository;
import kg.attractor.bookingsaas.service.BusinessValidator;
import kg.attractor.bookingsaas.service.impl.ServiceValidatorImpl;
import kg.attractor.bookingsaas.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Spy
    private UpdateUserMapperImpl updateUserMapper;

    @Spy
    private OutputUserMapperImpl outputUserMapper;

    @Spy
    private PageHolderWrapper pageHolderWrapper;

    @Mock
    private BusinessValidator businessValidator;

    @Mock
    private ServiceValidatorImpl serviceValidator;

    @AfterEach
    void checkMocks() {
        Mockito.verifyNoMoreInteractions(userRepository, updateUserMapper, outputUserMapper, pageHolderWrapper, businessValidator, serviceValidator);
    }

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Test
    void isUserEmailIsUnique() {
        // Given
        String email = "test@gmail.com";

        Mockito.when(userRepository.existsByEmail(email)).thenReturn(false);

        // When
        boolean result = userServiceImpl.isUserEmailIsUnique(email);

        // Then
        assertThat(result)
                .isTrue();

        Mockito.verify(userRepository).existsByEmail(email);
    }

    @Test
    void isUserPhoneNumberUnique() {
        String phoneNumber = "123456789";

        Mockito.when(userRepository.findUserByPhone(phoneNumber))
                .thenReturn(Optional.empty());

        boolean result = userServiceImpl.isUserPhoneNumberUnique(phoneNumber);

        assertThat(result)
                .isTrue();

        Mockito.verify(userRepository).findUserByPhone(phoneNumber);
    }

    @Test
    void updateUser() throws IOException {
        UpdateUserDto updateUserDto = UpdateUserDto.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .middleName("Smith")
                .phone("+123456789")
                .build();

        var userEntity = updateUserMapper.mapToEntity(updateUserDto);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userEntity, null, List.of(new SimpleGrantedAuthority(RoleEnum.CLIENT.name()))));

        Mockito.when(userRepository.findUserById(updateUserDto.getId()))
                .thenReturn(Optional.of(userEntity));

        UpdateUserDto updatedUser = userServiceImpl.updateUser(updateUserDto);

        assertThat(updatedUser)
                .isNotNull()
                .extracting("firstName", "lastName", "middleName", "phone")
                .containsExactly("John", "Doe", "Smith", "+123456789");

        Mockito.verify(updateUserMapper).mapToEntity(Mockito.any(UpdateUserDto.class));
        Mockito.verify(userRepository).findUserById(updateUserDto.getId());
        Mockito.verify(updateUserMapper).updateUser(userEntity, updateUserDto);
        Mockito.verify(updateUserMapper).mapToDto(userEntity);
    }
}