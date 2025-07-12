package kg.attractor.bookingsaas.service.impl;

import kg.attractor.bookingsaas.dto.BusinessReviewDto;
import kg.attractor.bookingsaas.dto.PageHolder;
import kg.attractor.bookingsaas.dto.mapper.impl.PageHolderWrapper;
import kg.attractor.bookingsaas.repository.BusinessReviewRepository;
import kg.attractor.bookingsaas.service.BusinessReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BusinessReviewServiceImpl implements BusinessReviewService {
    private final BusinessReviewRepository businessRepository;
    private final PageHolderWrapper pageHolderWrapper;

    @Override
    public PageHolder<BusinessReviewDto> findAllReviews(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("averageRating").descending());
        return pageHolderWrapper.wrapPageHolder(businessRepository.findAllReviews(pageable));
    }
}
