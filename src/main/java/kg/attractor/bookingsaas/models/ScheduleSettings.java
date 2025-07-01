package kg.attractor.bookingsaas.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "schedule_settings")
public class ScheduleSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "schedule_id", unique = true, nullable = false)
    private Schedule schedule;

    @Column(name = "break_between_bookings_in_minutes", nullable = false)
    private int breakBetweenBookings;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "settings", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<BreakPeriod> breakPeriods;
}