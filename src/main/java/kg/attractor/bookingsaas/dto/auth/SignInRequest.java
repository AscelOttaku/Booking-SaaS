package kg.attractor.bookingsaas.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на вход в систему")
public class SignInRequest {
    @Schema(description = "Почта пользователя", example = "john@example.com")
    @NotBlank
    @Email
    private String email;

    @Schema(description = "Пароль пользователя", example = "password123")
    @NotBlank
    private String password;
}
