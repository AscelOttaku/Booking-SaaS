package kg.attractor.bookingsaas.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class ScheduleTimeDto {
    private Long scheduleId;
    private final LocalTime startTime;
    private final LocalTime endTime;

    public ScheduleTimeDto(LocalTime startTime, LocalTime endTime, Long scheduleId) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.scheduleId = scheduleId;
    }
}