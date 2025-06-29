package kg.attractor.bookingsaas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import kg.attractor.bookingsaas.annotations.DurationAtLeastOneHour;
import kg.attractor.bookingsaas.markers.OnCreate;
import kg.attractor.bookingsaas.markers.OnUpdate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Schema(description = "Schedule data transfer object")
@Getter
@Setter
@Builder
@DurationAtLeastOneHour(message = "Start time and end time must be at least one hour apart", groups = {OnCreate.class, OnUpdate.class})
public class DailyScheduleDto {

    @Schema(description = "ID of the schedule", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    @Null(groups = OnCreate.class, message = "ID must be null when creating")
    @NotNull(groups = OnUpdate.class, message = "ID must not be null when updating")
    private Long id;

    @NotNull(groups = OnCreate.class, message = "Day of week ID must not be null")
    private Long dayOfWeekId;

    @NotNull(groups = OnCreate.class, message = "Service ID must not be null")
    private Long serviceId;

    @NotNull(groups = OnCreate.class, message = "Start time must not be null")
    private LocalTime startTime;

    @NotNull(groups = OnCreate.class, message = "End time must not be null")
    private LocalTime endTime;

    @Null(groups = OnCreate.class, message = "Availability must be null when creating")
    private Boolean isAvailable;

    @NotNull(groups = OnCreate.class, message = "Maximum booking size must not be null")
    @Min(value = 1, groups = {OnCreate.class, OnUpdate.class}, message = "Maximum booking size must be at least 1")
    private Integer maxBookingSize;

    @NotNull(groups = {OnCreate.class}, message = "Schedule settings must not be null")
    @Valid
    private ScheduleSettingsDto scheduleSettingsDto;
}