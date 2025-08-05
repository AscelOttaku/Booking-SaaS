package kg.attractor.bookingsaas.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import kg.attractor.bookingsaas.annotations.UniqueUserPhoneNumber;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class UserDto {
    @Schema(description = "Имя пользователя", example = "john")
    @NotBlank
    private String firstName;

    @Schema(description = "Отчество пользователя", example = "johndoe")
    private String middleName;

    @Schema(description = "Фамилия пользователя", example = "johndoe")
    private String lastName;

    @Schema(description = "телефон пользователя", example = "+996501198751")
    @NotBlank
    @UniqueUserPhoneNumber(message = "phone number required unique")
    private String phone;

    @Schema(description = "Дата рождения", example = "08.04.2014")
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate birthday;
}
