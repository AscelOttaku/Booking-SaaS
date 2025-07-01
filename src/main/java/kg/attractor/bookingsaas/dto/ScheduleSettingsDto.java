package kg.attractor.bookingsaas.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import kg.attractor.bookingsaas.markers.OnCreate;
import kg.attractor.bookingsaas.markers.OnUpdate;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleSettingsDto {

    @Null(groups = OnCreate.class, message = "ID must be null when creating")
    @NotNull(groups = OnUpdate.class, message = "ID must not be null when updating")
    private Long id;

    @NotNull(groups = OnCreate.class, message = "Break between bookings must not be null")
    @Min(value = 0, groups = OnCreate.class, message = "Break between bookings must be non-negative")
    private Integer breakBetweenBookings;

    private List<@Valid BreakPeriodDto> breakPeriodDto;
}