package kg.attractor.bookingsaas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import kg.attractor.bookingsaas.annotations.DurationAtLeastOneHour;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Schema(description = "Schedule data transfer object")
@Getter
@Setter
@Builder
@DurationAtLeastOneHour(message = "Start time and end time must be at least one hour apart")
public class ScheduleDto {

    @Schema(description = "ID of the schedule", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotNull(message = "Day of week ID must not be null")
    @Schema(description = "ID of the day of week", example = "3")
    private Long dayOfWeekId;

    @NotNull(message = "Start time must not be null")
    @Schema(description = "Start time (HH:mm:ss)", example = "09:00:00")
    private LocalTime startTime;

    @NotNull(message = "End time must not be null")
    @Schema(description = "End time (HH:mm:ss)", example = "18:00:00")
    private LocalTime endTime;

    @NotNull(message = "Availability must not be null")
    @Schema(description = "Is the schedule available", example = "true")
    private Boolean isAvailable;

    @NotNull(message = "Maximum booking size must not be null")
    @Min(value = 1, message = "Maximum booking size must be at least 1")
    @Schema(description = "Maximum booking size", example = "5")
    private Integer maxBookingSize;
}