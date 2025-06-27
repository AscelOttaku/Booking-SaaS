package kg.attractor.bookingsaas.service;

import kg.attractor.bookingsaas.dto.BusinessDto;
import kg.attractor.bookingsaas.dto.PageHolder;
import kg.attractor.bookingsaas.dto.bussines.BusinessCreateResponse;
import kg.attractor.bookingsaas.dto.bussines.BusinessInfoRequest;
import kg.attractor.bookingsaas.dto.bussines.BusinessSummaryResponse;

import java.util.List;

public interface BusinessService {
    List<BusinessSummaryResponse> searchBusiness(String name);

    BusinessCreateResponse createBusiness(BusinessInfoRequest businessInfo);

    void isBusinessExistById(Long id);

    void checkIfBusinessExistByTitle(String businessTitle);

    BusinessDto getBusinessById(Long id);

    boolean isBusinessTitleIsUnique(String title);

    PageHolder<BusinessSummaryResponse> getBusinessList(int page, int size);

    Long countBusinessesByUserId(Long authorizedUserId);
}
