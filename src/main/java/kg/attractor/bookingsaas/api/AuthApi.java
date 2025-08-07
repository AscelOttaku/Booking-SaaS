package kg.attractor.bookingsaas.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import kg.attractor.bookingsaas.dto.PasswordToken;
import kg.attractor.bookingsaas.dto.auth.AuthenticationResponse;
import kg.attractor.bookingsaas.dto.auth.SignInRequest;
import kg.attractor.bookingsaas.dto.auth.SignUpRequest;
import kg.attractor.bookingsaas.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Сервис аутентификации")
@RequiredArgsConstructor
public class AuthApi {
    private final AuthenticationService authenticationService;

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Регистрация нового пользователя",
            description = "Регистрация нового пользователя с предоставленными данными регистрации, " +
                    "такими как имя пользователя, почта и пароль. " +
                    "Этот конечный точка создает новый аккаунт пользователя в системе."
    )
    public AuthenticationResponse signUp(
            @Parameter(description = "Sign-up request containing user details", required = true)
            @Valid @RequestBody SignUpRequest signUpRequest) {
        return authenticationService.authenticate(signUpRequest);
    }

    @PostMapping("/sign-in")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Аутентификация пользователя",
            description = "Аунтифицирует пользователя и возвращает ответ с информацией об аутентификации," +
                    " включая токен доступа и другие связанные данные. " +
                    "Пользователь должен предоставить действительные учетные данные для входа," +
                    " например, почта и пароль, чтобы быть аутентифицированным."
    )
    public AuthenticationResponse signIn(
            @Parameter(description = "Sign-in request containing user credentials", required = true)
            @RequestBody SignInRequest signInRequest) {
        return authenticationService.signIn(signInRequest);
    }

    @PostMapping("/forgot-password")
    @ResponseStatus(HttpStatus.OK)
    public void processForgotPassword(
            @Parameter(description = "Email address for password reset", required = true)
            @RequestParam @NotBlank(message = "Email cannot be blank") String email) {
        authenticationService.makeResetPasswordLink(email);
    }

    @GetMapping("/reset-password-page")
    public String showResetPasswordPage(@RequestParam String token) {
        return "<!DOCTYPE html>"
                + "<html lang=\"en\">"
                + "<head>"
                + "<meta charset=\"UTF-8\">"
                + "<title>Reset Your Password</title>"
                + "<link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css\" rel=\"stylesheet\">"
                + "</head>"
                + "<body>"
                + "<div class=\"text-center\">"
                + "<h2>Reset Your Password</h2>"
                + "</div>"
                + "<div class=\"py-5 h-100\">"
                + "<div class=\"row d-flex align-items-center justify-content-center h-100\">"
                + "<div class=\"col-md-7 col-lg-5 col-xl-5\">"
                + "<form action=\"/api/auth/reset-password\" method=\"post\">"
                + "<input type=\"hidden\" name=\"token\" value=\"" + token + "\"/>"
                + "<div class=\"form-group\">"
                + "<input type=\"password\" class=\"form-control\" placeholder=\"Введите новый пароль\" autofocus id=\"password\" name=\"password\"/>"
                + "</div>"
                + "<p class=\"text-center\">"
                + "<button type=\"submit\" class=\"btn btn-primary mt-3\">Change Password</button>"
                + "</p>"
                + "</form>"
                + "</div>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";
    }

    @PostMapping("/reset-password")
    @ResponseStatus(HttpStatus.OK)
    public void processResetPassword(
            @Parameter(description = "New password token and password", required = true)
            @Valid PasswordToken passwordToken
    ) {
        authenticationService.processResetPassword(passwordToken);
    }
}
