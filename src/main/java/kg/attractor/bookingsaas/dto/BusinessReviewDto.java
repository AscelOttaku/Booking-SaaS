package kg.attractor.bookingsaas.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessReviewDto {
    private String businessName;
    private Long reviewCount;
    private Double averageRating;

    public BusinessReviewDto(String businessName, Long reviewCount, Double averageRating) {
        this.businessName = businessName;
        this.reviewCount = reviewCount;
        this.averageRating = averageRating;
    }
}
