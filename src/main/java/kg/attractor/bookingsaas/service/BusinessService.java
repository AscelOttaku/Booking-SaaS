package kg.attractor.bookingsaas.service;

import kg.attractor.bookingsaas.dto.PageHolder;
import kg.attractor.bookingsaas.dto.BusinessDto;

import java.util.List;

public interface BusinessService {
//    List<BusinessSummaryResponse> searchBusiness(String name);

//    BusinessCreateResponse createBusiness(BusinessInfoRequest businessInfo);

    BusinessDto findBusinessByTitle(String businessTitle);

    void isBusinessExistById(Long id);

    BusinessDto getBusinessById(Long id);

    boolean isBusinessTitleIsUnique(String title);

    PageHolder<BusinessDto> getBusinessList(int page, int size);

    Long countBusinessesByUserId(Long authorizedUserId);

    List<BusinessDto> findMostPopularFiveBusinessesByBusinessTitleContatining(String businessTitle);
}
