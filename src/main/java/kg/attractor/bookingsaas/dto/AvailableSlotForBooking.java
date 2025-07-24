package kg.attractor.bookingsaas.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvailableSlotForBooking {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
