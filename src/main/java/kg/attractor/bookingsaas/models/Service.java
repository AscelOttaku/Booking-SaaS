package kg.attractor.bookingsaas.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Getter
@Setter
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

    @OneToMany(mappedBy = "services", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<Book> books;

    @ManyToMany
    @JoinTable(
            name = "user_service",
            joinColumns = @JoinColumn(name = "service_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "service_id"})
    )
    private List<User> users;
}
