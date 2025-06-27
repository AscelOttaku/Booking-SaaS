package kg.attractor.bookingsaas.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "business_review")
public class BusinessReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "bigint")
    private User user;

    @Column(name = "review_text", nullable = false, length = 255)
    private String reviewText;

    @Column(nullable = false, precision = 2, scale = 1)
    private BigDecimal rating;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}