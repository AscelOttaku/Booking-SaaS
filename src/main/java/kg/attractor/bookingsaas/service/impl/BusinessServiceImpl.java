package kg.attractor.bookingsaas.service.impl;

import jakarta.persistence.*;
import kg.attractor.bookingsaas.dto.BusinessDto;
import kg.attractor.bookingsaas.dto.bussines.BusinessCreateResponse;
import kg.attractor.bookingsaas.dto.bussines.BusinessInfoRequest;
import kg.attractor.bookingsaas.dto.bussines.BusinessInfoResponse;
import kg.attractor.bookingsaas.dto.bussines.BusinessSummaryResponse;
import kg.attractor.bookingsaas.dto.mapper.impl.BusinessMapper;
import kg.attractor.bookingsaas.exceptions.NotFoundException;
import kg.attractor.bookingsaas.models.*;
import kg.attractor.bookingsaas.models.Business;
import kg.attractor.bookingsaas.repository.BusinessRepository;
import kg.attractor.bookingsaas.repository.ServiceRepository;
import kg.attractor.bookingsaas.service.BusinessService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static kg.attractor.bookingsaas.enums.RoleEnum.BUSINESS_OWNER;


@RequiredArgsConstructor
@org.springframework.stereotype.Service
@Slf4j
public class BusinessServiceImpl implements BusinessService {
    private final BusinessRepository businessRepository;
    private final ServiceRepository serviceRepository;
    private final BusinessMapper businessMapper;

    private User getAuthUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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
    public List<BusinessSummaryResponse> getBusinessList() {
        log.info("Получение списка бизнесов");
        List<Business> businessList = businessRepository.findAll();

        List<BusinessSummaryResponse> businessSummaryResponseList = getBusinessList(businessList);

        log.info("Список бизнесов получен успешно");
        return businessSummaryResponseList;
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
    public BusinessDto getBusinessById(Long id) {
        Business business = businessRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Business not found"));
        return businessMapper.toDto(business);
    }

    @Override
    public boolean isBusinessTitleIsUnique(String title) {
        return !businessRepository.existsByTitle(title);
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