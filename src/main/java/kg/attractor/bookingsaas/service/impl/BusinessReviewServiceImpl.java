package kg.attractor.bookingsaas.service.impl;

import kg.attractor.bookingsaas.dto.BusinessReviewDto;
import kg.attractor.bookingsaas.dto.PageHolder;
import kg.attractor.bookingsaas.dto.mapper.impl.PageHolderWrapper;
import kg.attractor.bookingsaas.repository.BusinessReviewRepository;
import kg.attractor.bookingsaas.service.BusinessReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BusinessReviewServiceImpl implements BusinessReviewService {
    private final BusinessReviewRepository businessRepository;
    private final PageHolderWrapper pageHolderWrapper;

    @Override
    public PageHolder<BusinessReviewDto> findAllReviews(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        var reviewPages = pageHolderWrapper.wrapPageHolder(businessRepository.findAllReviews(pageable));
        reviewPages.getContent().forEach(review -> log.info("Business Review Name: {}, Rating: {}, Count: {}",
                review.getBusinessName(), review.getAverageRating(), review.getReviewCount()));
        return reviewPages;
    }
}