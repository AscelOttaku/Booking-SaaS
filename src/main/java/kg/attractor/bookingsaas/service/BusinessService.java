package kg.attractor.bookingsaas.service;

import kg.attractor.bookingsaas.dto.PageHolder;
import kg.attractor.bookingsaas.dto.BusinessDto;

public interface BusinessService {
//    List<BusinessSummaryResponse> searchBusiness(String name);

//    BusinessCreateResponse createBusiness(BusinessInfoRequest businessInfo);

    void isBusinessExistById(Long id);

    BusinessDto getBusinessById(Long id);

    boolean isBusinessTitleIsUnique(String title);

    PageHolder<BusinessDto> getBusinessList(int page, int size);

    Long countBusinessesByUserId(Long authorizedUserId);
}
