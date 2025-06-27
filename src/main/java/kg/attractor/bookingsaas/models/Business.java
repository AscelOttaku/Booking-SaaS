package kg.attractor.bookingsaas.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "bussines")
public class Business {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 155)
    private String logo;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "city_id")
    private City city;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "business_category_id")
    private BusinessCategory businessCategory;

    @Column(name = "business_address", length = 255)
    private String businessAddress;

    @Column(name = "business_phone", length = 20)
    private String businessPhone;

    @Column(name = "business_email", length = 255)
    private String businessEmail;

    @OneToMany(mappedBy = "business")
    private List<Service> services;

    @PrePersist
    public void setCreatedAt() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void setUpdatedAt() {
        updatedAt = LocalDateTime.now();
    }
}