package kg.attractor.bookingsaas.service;

import kg.attractor.bookingsaas.dto.BusinessReviewDto;
import kg.attractor.bookingsaas.dto.PageHolder;

public interface BusinessReviewService {
    PageHolder<BusinessReviewDto> findAllReviews(int page, int size);
}
