package kg.attractor.bookingsaas.service.impl;

import kg.attractor.bookingsaas.config.jwt.JwtService;
import kg.attractor.bookingsaas.dto.PasswordToken;
import kg.attractor.bookingsaas.dto.ResetPasswordRequest;
import kg.attractor.bookingsaas.dto.auth.AuthenticationResponse;
import kg.attractor.bookingsaas.dto.auth.SignInRequest;
import kg.attractor.bookingsaas.dto.auth.SignUpRequest;
import kg.attractor.bookingsaas.event.publisher.UserMessageProducer;
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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserMessageProducer userMessageProducer;

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

    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    @Override
    public void makeResetPasswordLink(String email) {
        String token = UUID.randomUUID().toString();
        updateResetPasswordToken(token, email);

        String resetPasswordLink = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/auth/reset-password-page")
                .queryParam("token", token)
                .toUriString();

        var resetPasswordRequest = ResetPasswordRequest.builder()
                .link(resetPasswordLink)
                .email(email)
                .build();
        userMessageProducer.sendPasswordResetLink(resetPasswordRequest);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    @Override
    public void processResetPassword(PasswordToken passwordToken) {
        var user = userRepository.findByResetPasswordToken(passwordToken.getToken())
                .orElseThrow(() -> new NotFoundException("User not found by reset password token"));

        user.setPassword(passwordEncoder.encode(passwordToken.getPassword()));
        user.setResetPasswordLink(null);
    }

    private void updateResetPasswordToken(String token, String email) {
        if (!userRepository.existsByEmail(email)) {
            throw new NoSuchElementException("Email not found: " + email);
        }
        userRepository.setUserResetPasswordToken(token, email);
    }
}