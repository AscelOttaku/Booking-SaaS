package kg.attractor.bookingsaas.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import kg.attractor.bookingsaas.annotations.UniqueUserEmail;
import kg.attractor.bookingsaas.annotations.UniqueUserPhoneNumber;
import kg.attractor.bookingsaas.dto.user.UserDto;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на регистрацию пользователя")
public class SignUpRequest extends UserDto {

    @Schema(description = "Email пользователя", example = "john@example.com")
    @NotBlank
    @UniqueUserEmail(message = "Email should be unique")
    private String email;

    @Schema(description = "Пароль пользователя", example = "password123")
    @NotBlank
    @UniqueUserPhoneNumber(message = "Phone Number should be unique")
    private String password;

    @Schema(description = "Роль пользователя", example = "CLIENT")
    @NotBlank(message = "Blank roleName")
    private String roleName;
}
