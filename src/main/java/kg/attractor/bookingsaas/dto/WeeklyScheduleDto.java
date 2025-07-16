package kg.attractor.bookingsaas.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import kg.attractor.bookingsaas.markers.OnCreate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.sql.Update;

import java.util.List;

@Getter
@Setter
@Builder
public class WeeklyScheduleDto {

    @NotNull(message = "Service ID must not be null", groups = {Update.class, OnCreate.class})
    private Long serviceId;

    @NotEmpty(message = "Daily schedules must not be empty", groups = {Update.class, OnCreate.class})
    private List<@Valid DailyScheduleDto> dailySchedules;
}
