package kg.attractor.bookingsaas.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ScheduleAvailableSlots {
    private DailyScheduleDto dailyScheduleDto;
    private List<AvailableSlotForBooking> availableSlotForBooking;
}
