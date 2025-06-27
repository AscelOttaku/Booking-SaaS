package kg.attractor.bookingsaas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Schema(description = "Data Transfer Object for City")
public class CityDto {

    @Schema(description = "City identifier", example = "1")
    private Long id;

    @NotNull(message = "City name must not be null")
    @Size(min = 1, max = 100)
    @Schema(description = "Name of the city", example = "Bishkek")
    private String name;
}