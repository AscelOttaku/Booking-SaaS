package kg.attractor.bookingsaas.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NamedEntityGraph(
        name = "business-with-all",
        attributeNodes = {
                @NamedAttributeNode("user"),
                @NamedAttributeNode("city"),
                @NamedAttributeNode("businessCategory"),
                @NamedAttributeNode(value = "services", subgraph = "services-subgraph")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "services-subgraph",
                        attributeNodes = {
                                @NamedAttributeNode("business"),
                        }
                )
        }
)

@Getter
@Setter
@Entity
@Table(name = "bussines")
public class Business {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "title", unique = true, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "logo")
    private String logo;

    @Column(name = "business_address")
    private String businessAddress;

    @Column(name = "business_phone")
    private String businessPhone;

    @Column(name = "business_email")
    private String businessEmail;

    @ManyToOne(optional = false)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @ManyToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "business_category_id", nullable = false)
    private BusinessCategory businessCategory;

    @OneToMany(mappedBy = "business", fetch = FetchType.LAZY)
    private List<BusinessUnderCategory> businessUnderCategories;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT now()", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT now()")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "business", fetch = FetchType.LAZY)
    private List<Service> services = new ArrayList<>();

    @PrePersist
    public void setCreatedAt() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void setUpdatedAt() {
        updatedAt = LocalDateTime.now();
    }
}
