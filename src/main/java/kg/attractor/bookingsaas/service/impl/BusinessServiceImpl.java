package kg.attractor.bookingsaas.service.impl;

import kg.attractor.bookingsaas.dto.BusinessDto;
import kg.attractor.bookingsaas.dto.PageHolder;
import kg.attractor.bookingsaas.dto.mapper.impl.BusinessMapper;
import kg.attractor.bookingsaas.dto.mapper.impl.PageHolderWrapper;
import kg.attractor.bookingsaas.models.Business;
import kg.attractor.bookingsaas.repository.BusinessRepository;
import kg.attractor.bookingsaas.service.AuthorizedUserService;
import kg.attractor.bookingsaas.service.BusinessService;
import kg.attractor.bookingsaas.service.BusinessValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.Assert;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@org.springframework.stereotype.Service
@Slf4j
public class BusinessServiceImpl implements BusinessService, BusinessValidator {
    private final BusinessRepository businessRepository;
    private final BusinessMapper businessMapper;
    private final PageHolderWrapper pageHolderWrapper;
    private final AuthorizedUserService authorizedUserService;

    @Override
    public void checkIsBusinessBelongsToAuthUser(Long businessId) {
        Assert.notNull(businessId, "Business ID must not be null");
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new NoSuchElementException("Business not found by ID: " + businessId));
        Long ownerId = business.getUser().getId();
        if (!ownerId.equals(authorizedUserService.getAuthorizedUserId())) {
            throw new SecurityException("You do not have permission to access this business");
        }
    }

    @Override
    public void checkIsBusinessBelongsToAuthUser(String businessTitle) {
        Assert.hasText(businessTitle, "Business title must not be null or blank");
        Business business = businessRepository.findByTitle(businessTitle)
                .orElseThrow(() -> new NoSuchElementException("Business not found by title: " + businessTitle));
        Long ownerId = business.getUser().getId();
        if (!ownerId.equals(authorizedUserService.getAuthorizedUserId())) {
            throw new SecurityException("You do not have permission to access this business");
        }
    }

    @Override
    public PageHolder<BusinessDto> getBusinessList(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<BusinessDto> businessPage = businessRepository.findBusiness(pageRequest)
                .map(businessMapper::toDto);
        return pageHolderWrapper.wrapPageHolder(businessPage);
    }

    @Override
    public BusinessDto findBusinessByTitle(String businessTitle) {
        Assert.hasText(businessTitle, "Business title must not be null or blank");
        Business business = businessRepository.findByTitle(businessTitle)
                .orElseThrow(() -> new NoSuchElementException("Business not found by title: " + businessTitle));
        return businessMapper.toDto(business);
    }

    @Override
    public void isBusinessExistById(Long id) {
        if (!businessRepository.existsById(id))
            throw new NoSuchElementException("Business does not exist");
    }

    @Override
    public void checkIfBusinessExistByTitle(String businessTitle) {
        if (!businessRepository.existsByTitle(businessTitle))
            throw new NoSuchElementException("Business does not exist by title " + businessTitle);
    }

    @Override
    public BusinessDto getBusinessById(Long id) {
        Business business = businessRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Business not found"));
        return businessMapper.toDto(business);
    }

    @Override
    public boolean isBusinessTitleIsUnique(String title) {
        return !businessRepository.existsByTitle(title);
    }

    @Override
    public Long countBusinessesByUserId(Long authorizedUserId) {
        Assert.isTrue(authorizedUserId != null && authorizedUserId > 0, "Authorized user ID must be valid");
        return businessRepository.countBusinessesByUserId(authorizedUserId);
    }

    @Override
    public List<BusinessDto> findMostPopularFiveBusinessesByBusinessTitleContatining(String businessTitle) {
        Assert.hasText(businessTitle, "Business title must not be null or blank");
        return businessRepository.findByTitleContaining(businessTitle).stream()
                .collect(Collectors.toMap(Function.identity(),
                        business -> business.getServices().stream()
                                .flatMap(service -> service.getSchedules().stream())
                                .mapToInt(schedule -> schedule.getBooks().size())
                                .sum())
                )
                .entrySet()
                .stream()
                .sorted(Map.Entry.<Business, Integer>comparingByValue().reversed())
                .limit(5)
                .map(entry -> businessMapper.toDto(entry.getKey()))
                .toList();
    }
}