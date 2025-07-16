package kg.attractor.bookingsaas.service;

import jakarta.validation.Valid;
import kg.attractor.bookingsaas.dto.auth.AuthenticationResponse;
import kg.attractor.bookingsaas.dto.auth.SignInRequest;
import kg.attractor.bookingsaas.dto.auth.SignUpRequest;

public interface AuthenticationService {
    AuthenticationResponse authenticate(@Valid SignUpRequest signUpRequest);

    AuthenticationResponse signIn(SignInRequest signInRequest);
}
