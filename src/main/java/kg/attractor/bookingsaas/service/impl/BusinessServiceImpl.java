package kg.attractor.bookingsaas.service.impl;

import kg.attractor.bookingsaas.dto.BusinessDto;
import kg.attractor.bookingsaas.dto.PageHolder;
import kg.attractor.bookingsaas.dto.bussines.BusinessSummaryResponse;
import kg.attractor.bookingsaas.dto.mapper.impl.BusinessMapper;
import kg.attractor.bookingsaas.dto.mapper.impl.PageHolderWrapper;
import kg.attractor.bookingsaas.models.Business;
import kg.attractor.bookingsaas.models.BusinessUnderCategory;
import kg.attractor.bookingsaas.repository.BusinessRepository;
import kg.attractor.bookingsaas.service.AuthorizedUserService;
import kg.attractor.bookingsaas.service.BusinessService;
import kg.attractor.bookingsaas.service.BusinessValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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

    private List<BusinessSummaryResponse> getBusinessList(List<Business> businessList) {
        List<BusinessSummaryResponse> businessSummaryResponseList = new ArrayList<>();

        for (Business business : businessList) {
            BusinessSummaryResponse businessSummaryResponse = new BusinessSummaryResponse();
            ArrayList<String> underCategories = new ArrayList<>();

            for (BusinessUnderCategory businessUnderCategory : business.getBusinessUnderCategories()) {
                underCategories.add(businessUnderCategory.getName());
            }

            businessSummaryResponse.setId(business.getId());
            businessSummaryResponse.setLogo(business.getLogo());
            businessSummaryResponse.setAddress(business.getBusinessAddress());
            businessSummaryResponse.setCity(business.getCity().getName());
            businessSummaryResponse.setBusinessCategory(business.getBusinessCategory().getName());
            businessSummaryResponse.setBusinessUnderCategory(underCategories);
            businessSummaryResponse.setTitle(business.getTitle());
            businessSummaryResponse.setDescription(business.getDescription());
            businessSummaryResponseList.add(businessSummaryResponse);
        }
        return businessSummaryResponseList;
    }

    @Override
    public PageHolder<BusinessSummaryResponse> getBusinessList(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<BusinessSummaryResponse> businessPage = businessRepository.findAll(pageRequest)
                .map(business -> {
                    BusinessSummaryResponse response = new BusinessSummaryResponse();
                    response.setId(business.getId());
                    response.setLogo(business.getLogo());
                    response.setAddress(business.getBusinessAddress());
                    response.setCity(business.getCity().getName());
                    response.setBusinessCategory(business.getBusinessCategory().getName());
//                    response.setBusinessUnderCategory(businessRepository.getBusinessUnderCategoryNames(business.getId()));
                    response.setTitle(business.getTitle());
                    response.setDescription(business.getDescription());
                    return response;
                });
        return pageHolderWrapper.wrapPageHolder(businessPage);
    }

//    @Override
//    public List<BusinessSummaryResponse> searchBusiness(String name) {
//        log.info("Поиск бизнеса по названию: {}", name);
//        List<Business> businessList = businessRepository.findByNameContaining(name);
//        List<BusinessSummaryResponse> businessSummaryResponseList = new ArrayList<>();
//
//        for (Business business : businessList) {
//            BusinessSummaryResponse businessSummaryResponse = new BusinessSummaryResponse();
//            businessSummaryResponse.setId(business.getId());
//            businessSummaryResponse.setTitle(business.getTitle());
//            businessSummaryResponse.setDescription(business.getDescription());
//            businessSummaryResponseList.add(businessSummaryResponse);
//        }
//        log.info("Бизнесы найдены успешно");
//        return businessSummaryResponseList;
//    }
//
//    @Override
//    public BusinessInfoResponse getBusinessInfo(Long businessId) {
//        log.info("Получение информации о бизнесе: {}", businessId);
//        Business business = businessRepository.findById(businessId).orElseThrow(() -> new NotFoundException("Бизнес не найден по id: " + businessId));
//        BusinessInfoResponse businessInfoResponse = new BusinessInfoResponse();
//
//        List<kg.attractor.bookingsaas.models.Service> serviceList = serviceRepository.findAllByBusinessId(business.getId());
//        List<String> services =  serviceList.stream().map(kg.attractor.bookingsaas.models.Service::getServiceName).toList();
//
//        businessInfoResponse.setTitle(business.getTitle());
//        businessInfoResponse.setDescription(business.getDescription());
//        businessInfoResponse.setCreatedAt(business.getCreatedAt());
//        businessInfoResponse.setServices(services);
//
//        log.info("Информация о бизнесе получена успешно");
//        return businessInfoResponse;
//    }
//
//    @Override
//    public BusinessCreateResponse createBusiness(BusinessInfoRequest businessInfo) {
//        log.info("Создание бизнеса: {}", businessInfo);
//        if (businessRepository.existsByTitle(businessInfo.getTitle())) {
//            throw new IllegalArgumentException("Бизнес с таким названием уже существует");
//        }
//        User owner = getAuthUser();
//
//        if (owner.getRole().getRoleName() != BUSINESS_OWNER){
//            throw new IllegalArgumentException("Только пользователи с ролью " + BUSINESS_OWNER + " могут создавать бизнесы");
//        }
//
//        Business business = new Business();
//        business.setUser(owner);
//        business.setTitle(businessInfo.getTitle());
//        business.setDescription(businessInfo.getDescription());
//        businessRepository.save(business);
//
//        BusinessCreateResponse businessCreateResponse = new BusinessCreateResponse();
//        businessCreateResponse.setId(business.getId());
//        businessCreateResponse.setTitle(business.getTitle());
//        businessCreateResponse.setDescription(business.getDescription());
//
//        log.info("Бизнес создан успешно");
//
//        return businessCreateResponse;
//    }

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
}
//@Getter
//@Setter
//@Entity
//@Table(name = "bussines")
//public class Business {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(optional = false)
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
//
//    private String title;
//
//    @Column(columnDefinition = "TEXT")
//    private String description;
//
//    @Column(name = "logo")
//    private String logo;
//
//    @Column(name = "business_address")
//    private String businessAddress;
//
//    @Column(name = "business_phone")
//    private String businessPhone;
//
//    @Column(name = "business_email")
//    private String businessEmail;
//
//    @ManyToOne(optional = false)
//    @JoinColumn(name = "city_id", nullable = false)
//    private City city;
//
//    @ManyToOne(optional = false)
//    @JoinColumn(name = "business_category_id", nullable = false)
//    private BusinessCategory businessCategory;
//
//    @OneToMany(mappedBy = "business")
//    private List<BusinessUnderCategory> businessUnderCategories;
//
//    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT now()", updatable = false)
//    private LocalDateTime createdAt;
//
//    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT now()")
//    private LocalDateTime updatedAt;
//
//    @OneToMany(mappedBy = "business")
//    private List<Service> services;
//
//    @PrePersist
//    public void setCreatedAt() {
//        createdAt = LocalDateTime.now();
//    }
//
//    @PreUpdate
//    public void setUpdatedAt() {
//        updatedAt = LocalDateTime.now();
//    }
//}