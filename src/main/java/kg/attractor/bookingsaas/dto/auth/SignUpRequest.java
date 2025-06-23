package kg.attractor.bookingsaas.dto.auth;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kg.attractor.bookingsaas.enums.RoleEnum;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Schema(description = "Запрос на регистрацию пользователя")
public class SignUpRequest {
    @Schema(description = "Имя пользователя", example = "johndoe")
    @NotBlank
    private String firstName;

    @Schema(description = "Отчество пользователя", example = "johndoe")
    @NotBlank
    private String middleName;

    @Schema(description = "Фамилия пользователя", example = "johndoe")
    @NotBlank
    private String lastName;

    @Schema(description = "телефон пользователя", example = "+996501198751")
    @NotBlank
    private String phone;

    @Schema(description = "Дата рождения", example = "08.04.2014")
    @NotBlank
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate birthday;

    @Schema(description = "Email пользователя", example = "john@example.com")
    @NotBlank
    @Email
    private String email;
    @Schema(description = "Пароль пользователя", example = "password123")
    @NotBlank
    private String password;
    @Schema(description = "Роль пользователя", example = "USER")
    @NotNull
    private RoleEnum role;
}
