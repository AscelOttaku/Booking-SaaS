package kg.attractor.bookingsaas.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import kg.attractor.bookingsaas.annotations.UniqueUserPhoneNumber;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public abstract class UserDto {
    @Schema(description = "Имя пользователя", example = "john")
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
    @UniqueUserPhoneNumber(message = "phone number required unique")
    private String phone;

    @Schema(description = "Дата рождения", example = "08.04.2014")
    @NotBlank
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate birthday;
}
