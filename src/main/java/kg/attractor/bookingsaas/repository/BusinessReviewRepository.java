package kg.attractor.bookingsaas.repository;

import kg.attractor.bookingsaas.dto.BusinessReviewDto;
import kg.attractor.bookingsaas.models.BusinessReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessReviewRepository extends JpaRepository<BusinessReview, Long> {

    @Query(value = "SELECT new kg.attractor.bookingsaas.dto.BusinessReviewDto(b.title, COUNT(br), AVG(br.rating)) " +
            "FROM BusinessReview br JOIN br.business b " +
            "GROUP BY b.title " +
            "order by AVG(br.rating) DESC",
            countQuery = "SELECT COUNT(DISTINCT B.title) " +
                    "FROM Business B ")
    Page<BusinessReviewDto> findAllReviews(Pageable pageable);
}
