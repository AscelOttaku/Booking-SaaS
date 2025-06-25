package kg.attractor.bookingsaas.service;

import kg.attractor.bookingsaas.dto.bussines.BusinessCreateResponse;
import kg.attractor.bookingsaas.dto.bussines.BusinessInfoRequest;
import kg.attractor.bookingsaas.dto.bussines.BusinessInfoResponse;
import kg.attractor.bookingsaas.dto.bussines.BusinessSummaryResponse;

import java.util.List;

public interface BusinessService {
    List<BusinessSummaryResponse> getBusinessList();

    List<BusinessSummaryResponse> searchBusiness(String name);

    BusinessInfoResponse getBusinessInfo(Long businessId);

    BusinessCreateResponse createBusiness(BusinessInfoRequest businessInfo);
}
