package kg.attractor.bookingsaas.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "schedule")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "bussines_service_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Service bussinesService;

    @JoinColumn(name = "day_of_week_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private DayOfWeek dayOfWeek;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "is_available")
    private boolean isAvailable;

    @Column(name = "max_booking_size")
    private int maxBookingSize;
}
