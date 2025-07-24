package kg.attractor.bookingsaas.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "holiday")
public class Holiday {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "local_name", length = 255)
    private String localName;

    @Column(length = 255, nullable = false, unique = true)
    private String name;

    @Column(name = "country_code", nullable = false, length = 10)
    private String countryCode;

    @Column(nullable = false)
    private Boolean global;

    @ElementCollection
    @CollectionTable(name = "holiday_types", joinColumns = @JoinColumn(name = "holiday_id"))
    @Column(name = "type", length = 100, nullable = false)
    private List<String> types;
}