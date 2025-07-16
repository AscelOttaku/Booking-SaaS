package kg.attractor.bookingsaas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "Day of week data transfer object")
@Getter
@Setter
@Builder
public class DayOfWeekDto {
    @NotBlank(message = "Day name must not be blank")
    @Schema(description = "Name of the day", example = "Monday", required = true)
    private String name;

    @NotNull(message = "Working status must not be null")
    @Schema(description = "Is this day a working day", example = "true", required = true)
    private Boolean isWorking;
}