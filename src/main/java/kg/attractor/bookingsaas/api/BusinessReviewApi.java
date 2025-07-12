package kg.attractor.bookingsaas.api;

import io.swagger.v3.oas.annotations.Operation;
import kg.attractor.bookingsaas.dto.BusinessReviewDto;
import kg.attractor.bookingsaas.dto.PageHolder;
import kg.attractor.bookingsaas.service.BusinessReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/business/reviews")
@RequiredArgsConstructor
public class BusinessReviewApi {

    private final BusinessReviewService businessReviewService;

    @GetMapping
    @Operation(
            summary = "Get all business reviews",
            description = "Returns a paginated list of business reviews sorted by average rating"
    )
    public PageHolder<BusinessReviewDto> getAllReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return businessReviewService.findAllReviews(page, size);
    }
}