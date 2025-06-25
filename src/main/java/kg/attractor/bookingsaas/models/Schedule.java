package kg.attractor.bookingsaas.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Table(name = "schedule")
@Getter
@Setter
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "day_of_week_id", nullable = false)
    private DayOfWeekEntity dayOfWeek;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "bussines_service_id", nullable = false)
    private Service service;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable;

    @Column(name = "max_booking_size", nullable = false)
    private Integer maxBookingSize;
}