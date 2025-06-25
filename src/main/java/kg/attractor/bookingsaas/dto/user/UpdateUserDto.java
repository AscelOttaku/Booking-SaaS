package kg.attractor.bookingsaas.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
public class UpdateUserDto extends UserDto {

    @Schema(description = "user id", example = "1")
    @NotNull(message = "id must not be null")
    @Positive(message = "id must be positive")
    private Long id;

    @Schema(description = "Image file")
    private MultipartFile image;
}
