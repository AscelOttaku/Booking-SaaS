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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id")
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "review_text")
    private String reviewText;

    @Column(columnDefinition = "numeric")
    private BigDecimal rating;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT now()", updatable = false)
    private LocalDateTime createdAt;
}
