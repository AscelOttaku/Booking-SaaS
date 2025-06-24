package kg.attractor.bookingsaas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import kg.attractor.bookingsaas.annotations.DurationBookTimes;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@DurationBookTimes(durationInSeconds = 600, message = "Duration between startedAt and finishedAt should be more then 10 minutes")
public class CreateBookDto {
    @Schema(description = "Service ID", example = "2")
    @NotNull(message = "serviceId must not be null")
    @Positive(message = "serviceId must be positive")
    private Long serviceId;

    @Schema(description = "Booking start time", example = "2024-07-01T10:00:00")
    @NotNull(message = "startedAt must not be null")
    private LocalDateTime startedAt;

    @Schema(description = "Booking finish time", example = "2024-07-01T11:00:00")
    @NotNull(message = "finishedAt must not be null")
    @Future(message = "finishedAt must be in the future")
    private LocalDateTime finishedAt;
}