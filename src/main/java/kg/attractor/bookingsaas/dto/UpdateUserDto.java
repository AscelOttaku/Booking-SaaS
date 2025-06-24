package kg.attractor.bookingsaas.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class UpdateUserDto {
    @Schema(description = "user id", example = "1")
    @NotNull(message = "id must not be null")
    @Positive(message = "id must be positive")
    private Long id;

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

    @Schema(description = "Image file")
    private MultipartFile image;
}
