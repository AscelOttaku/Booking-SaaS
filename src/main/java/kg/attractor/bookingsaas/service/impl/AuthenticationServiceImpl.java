package kg.attractor.bookingsaas.service.impl;

import kg.attractor.bookingsaas.config.jwt.JwtService;
import kg.attractor.bookingsaas.dto.auth.AuthenticationResponse;
import kg.attractor.bookingsaas.dto.auth.SignInRequest;
import kg.attractor.bookingsaas.dto.auth.SignUpRequest;
import kg.attractor.bookingsaas.exceptions.AlreadyExistsException;
import kg.attractor.bookingsaas.exceptions.InvalidPasswordException;
import kg.attractor.bookingsaas.exceptions.NotFoundException;
import kg.attractor.bookingsaas.models.User;
import kg.attractor.bookingsaas.repository.RoleRepository;
import kg.attractor.bookingsaas.repository.UserRepository;
import kg.attractor.bookingsaas.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthenticationResponse authenticate(SignUpRequest request) {
        log.info("Идет регистрация пользователя с почтой: {}", request.getEmail());
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException("Пользователь с почтой " + request.getEmail() + " уже существует");
        }

        User users = new User();
        users.setFirstName(request.getFirstName());
        users.setLastName(request.getLastName());
        users.setMiddleName(request.getMiddleName());
        users.setPassword(passwordEncoder.encode(request.getPassword()));
        users.setPhone(request.getPhone());
        users.setBirthday(request.getBirthday());
        users.setEmail(request.getEmail());
        users.setRole(roleRepository.findByRoleName(request.getRoleName())
                .orElseThrow(() -> new NoSuchElementException("role not found by name " + request.getRoleName())));

        userRepository.save(users);

        String jwtToken = jwtService.generateToken(users.getEmail());
        log.info("Пользователь с почтой {} успешно зарегистрирован", request.getEmail());

        return new AuthenticationResponse(jwtToken, users.getEmail(), users.getRole().getRoleName());
    }

    @Override
    public AuthenticationResponse signIn(SignInRequest signInRequest) {
        log.info("Идет вход пользователя с почтой: {}", signInRequest.getEmail());

        User user = userRepository.findByEmail(signInRequest.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!passwordEncoder.matches(signInRequest.getPassword(), user.getPassword())) {
            log.warn("Пароль пользователя с почтой {} не совпадает", signInRequest.getEmail());
            throw new InvalidPasswordException("Invalid password");
        }

        String jwtToken = jwtService.generateToken(user.getEmail());
        log.info("Пользователь с почтой {} успешно вошел в систему", signInRequest.getEmail());
        return new AuthenticationResponse(jwtToken, user.getEmail(), user.getRole().getRoleName());
    }
}