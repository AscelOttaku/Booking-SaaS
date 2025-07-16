package kg.attractor.bookingsaas.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "services")
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "service_name", nullable = false)
    private String serviceName;

    @ManyToOne(optional = false)
    @JoinColumn(name = "bussines_id", nullable = false)
    private Business business;

    @Column(name = "duration_in_min", nullable = false)
    private Integer durationInMinutes;

    @Column(name = "price", nullable = false, columnDefinition = "decimal(10,2)")
    private BigDecimal price;

    @OneToMany(mappedBy = "service", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, orphanRemoval = true)
    public List<Schedule> schedules;
}
