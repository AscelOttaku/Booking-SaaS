package kg.attractor.bookingsaas.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class WeeklyScheduleDto {
    private Long serviceId;
    private List<DailyScheduleDto> dailySchedules;
}
