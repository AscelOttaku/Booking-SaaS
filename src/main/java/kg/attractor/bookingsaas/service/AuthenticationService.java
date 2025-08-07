package kg.attractor.bookingsaas.service;

import kg.attractor.bookingsaas.dto.PasswordToken;
import kg.attractor.bookingsaas.dto.auth.AuthenticationResponse;
import kg.attractor.bookingsaas.dto.auth.SignInRequest;
import kg.attractor.bookingsaas.dto.auth.SignUpRequest;

public interface AuthenticationService {
    AuthenticationResponse authenticate(SignUpRequest signUpRequest);

    AuthenticationResponse signIn(SignInRequest signInRequest);

    void makeResetPasswordLink(String email);

    void processResetPassword(PasswordToken passwordToken);
}
