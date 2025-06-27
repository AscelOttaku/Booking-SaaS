package kg.attractor.bookingsaas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Schema(description = "DTO for linking a schedule to a service")
@Getter
@Setter
@Builder
public class ScheduleServiceDto {

    @Schema(description = "ID of the schedule-service link", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotNull(message = "Service ID must not be null")
    @Positive(message = "Schedule ID must be a positive number")
    @Schema(description = "ID of the service", example = "5", required = true)
    private Long serviceId;

    @NotNull(message = "Schedule list must not be null")
    @NotEmpty(message = "Schedule list must not be empty")
    List<@Valid DailyScheduleDto> dailyScheduleDtos;
}