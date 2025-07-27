package kg.attractor.bookingsaas.service.impl;

import kg.attractor.bookingsaas.config.jwt.JwtService;
import kg.attractor.bookingsaas.dto.auth.AuthenticationResponse;
import kg.attractor.bookingsaas.dto.auth.SignInRequest;
import kg.attractor.bookingsaas.dto.auth.SignUpRequest;
import kg.attractor.bookingsaas.enums.RoleEnum;
import kg.attractor.bookingsaas.exceptions.AlreadyExistsException;
import kg.attractor.bookingsaas.exceptions.InvalidPasswordException;
import kg.attractor.bookingsaas.exceptions.NotFoundException;
import kg.attractor.bookingsaas.models.Role;
import kg.attractor.bookingsaas.models.User;
import kg.attractor.bookingsaas.repository.RoleRepository;
import kg.attractor.bookingsaas.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private JwtService jwtService;

    @Spy
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    private SignUpRequest signUpRequest;
    private SignInRequest signInRequest;

    @BeforeEach
    void setUp() {
        signUpRequest = SignUpRequest.builder()
                .email("john@gmail.com")
                .password("password123")
                .roleName("CLIENT")
                .build();

        signInRequest = SignInRequest.builder()
                .email("john@gmail.com")
                .password("password123")
                .build();
    }

    @AfterEach
    void afterEach() {
        Mockito.verifyNoMoreInteractions(userRepository, roleRepository, jwtService);
    }

    @Test
    void authenticate() {
        // Given
        when(userRepository.existsByEmail("john@gmail.com"))
                .thenReturn(false);

        Role role = new Role();
        role.setRoleName(RoleEnum.CLIENT);
        when(roleRepository.findByRoleName(signUpRequest.getRoleName()))
                .thenReturn(Optional.of(role));

        when(userRepository.save(Mockito.any(User.class))).thenAnswer(invocation ->
                invocation.getArgument(0));

        when(jwtService.generateToken(signUpRequest.getEmail()))
                .thenReturn("mocked-jwt-token");

        // When
        AuthenticationResponse response = authenticationService.authenticate(signUpRequest);

        // Then
        Assertions.assertThat(response)
                .isNotNull()
                .extracting(AuthenticationResponse::getUsername)
                .isEqualTo("john@gmail.com");

        Assertions.assertThat(response.getToken())
                .isNotBlank()
                .isEqualTo("mocked-jwt-token");

        Assertions.assertThat(response.getRole())
                .isEqualTo(RoleEnum.CLIENT);

        verify(userRepository).existsByEmail(signUpRequest.getEmail());
        verify(roleRepository).findByRoleName(signUpRequest.getRoleName());
        verify(userRepository).save(Mockito.any(User.class));
        verify(jwtService).generateToken(signUpRequest.getEmail());
    }

    @Test
    void authenticate_userAlreadyExists() {
        // Given
        when(userRepository.existsByEmail("john@gmail.com"))
                .thenReturn(true);

        // Then
        Assertions.assertThatThrownBy(() -> {
                    authenticationService.authenticate(signUpRequest);
                })
                .isInstanceOf(AlreadyExistsException.class)
                .hasMessage("Пользователь с почтой " + signUpRequest.getEmail() + " уже существует");

        verify(userRepository).existsByEmail("john@gmail.com");
    }

    @Test
    void signIn() {
        // Given
        Role role = new Role();
        role.setRoleName(RoleEnum.CLIENT);

        User user = new User();
        user.setEmail("john@gmail.com");
        user.setRole(role);
        user.setPassword(passwordEncoder.encode("password123"));

        when(userRepository.findByEmail(signInRequest.getEmail()))
                .thenReturn(Optional.of(user));

        when(jwtService.generateToken(signInRequest.getEmail()))
                .thenReturn("mocked-jwt-token");

        // When
        AuthenticationResponse response = authenticationService.signIn(signInRequest);

        // Then
        Assertions.assertThat(response)
                .isNotNull()
                .extracting(AuthenticationResponse::getUsername)
                .isEqualTo(signInRequest.getEmail());

        Assertions.assertThat(response.getToken())
                .isNotBlank()
                .isEqualTo("mocked-jwt-token");

        verify(userRepository).findByEmail(signInRequest.getEmail());
        verify(jwtService).generateToken(signInRequest.getEmail());
        verify(passwordEncoder).matches(signInRequest.getPassword(), user.getPassword());
    }

    @Test
    void signIn_userNotFound() {
        // Given
        when(userRepository.findByEmail(signInRequest.getEmail()))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThatThrownBy(() -> {
                    authenticationService.signIn(signInRequest);
                })
                .isInstanceOf(NotFoundException.class)
                .hasMessage("User not found");

        verify(userRepository).findByEmail(signInRequest.getEmail());
    }

    @Test
    void signIn_invalidPassword() {
        // Given
        User user = new User();
        user.setEmail("john@gmail.com");
        user.setPassword(passwordEncoder.encode("Invalid Password"));

        when(userRepository.findByEmail(signInRequest.getEmail()))
                .thenReturn(Optional.of(user));

        // Then
        assertThatThrownBy(() -> authenticationService.signIn(signInRequest))
                .isInstanceOf(InvalidPasswordException.class)
                .hasMessage("Invalid password");

        verify(userRepository).findByEmail(signInRequest.getEmail());
        verify(passwordEncoder).matches(signInRequest.getPassword(), user.getPassword());
    }
}