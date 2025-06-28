package kg.attractor.bookingsaas.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Null;
import kg.attractor.bookingsaas.markers.OnCreate;
import kg.attractor.bookingsaas.markers.OnUpdate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@Builder
public class BreakPeriodDto {

    @Null(groups = OnCreate.class, message = "ID must be null when creating")
    @NotNull(groups = OnUpdate.class, message = "ID must not be null when updating")
    private Long id;

    @NotNull(groups = OnCreate.class, message = "Start time must not be null")
    private LocalTime start;

    @NotNull(groups = OnCreate.class, message = "End time must not be null")
    private LocalTime end;

    @NotNull(groups = OnCreate.class, message = "ScheduleSettings ID must not be null")
    @Positive(groups = OnCreate.class, message = "ScheduleSettings ID must be a positive number")
    private Long scheduleSettingsId;
}