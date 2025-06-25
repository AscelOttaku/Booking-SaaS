package kg.attractor.bookingsaas.api;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kg.attractor.bookingsaas.dto.auth.AuthenticationResponse;
import kg.attractor.bookingsaas.dto.auth.SignUpRequest;
import kg.attractor.bookingsaas.dto.bussines.BusinessCreateResponse;
import kg.attractor.bookingsaas.dto.bussines.BusinessInfoRequest;
import kg.attractor.bookingsaas.dto.bussines.BusinessInfoResponse;
import kg.attractor.bookingsaas.dto.bussines.BusinessSummaryResponse;
import kg.attractor.bookingsaas.models.Business;
import kg.attractor.bookingsaas.models.User;
import kg.attractor.bookingsaas.service.BusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static kg.attractor.bookingsaas.enums.RoleEnum.BUSINESS_OWNER;

@RestController
@RequestMapping("/api/business")
@Tag(name = "Сервис Управления Бизнесом")
@RequiredArgsConstructor
public class BusinessApi {

    private final BusinessService businessService;


    @GetMapping("/getAllBusiness")
    @Operation(
            summary = "Получения списка бизнесов",
            description = "Получение списка бизнесов, которые доступны текущему пользователю. ")
    public List<BusinessSummaryResponse> getBusinessList() {
        return businessService.getBusinessList();
    }

    @GetMapping("/searchBusiness")
    @Operation(
            summary = "Получение списка бизнесов с фильтрацией",
            description = "Получение списка бизнесов, с возможностью поиска по названию")
    public List<BusinessSummaryResponse> searchBusiness(
            @Parameter(description = "Название бизнеса") @RequestParam(required = false) String name){
        return businessService.searchBusiness(name);
    }

    @GetMapping("/{businessId}")
    @Operation(
            summary = "Получение информации о бизнесе",
            description = "Получение информации о бизнесе по его ID")
    public BusinessInfoResponse getBusinessInfo(@PathVariable("businessId") Long businessId) {
        return businessService.getBusinessInfo(businessId);
    }

    @PreAuthorize("hasAuthority('BUSINESS_OWNER')")
    @PostMapping("/createBusiness")
    @Operation(
            summary = "Создание бизнеса",
            description = "Создание бизнеса с указанием названия и описания," +
                    " создать бизнес сможет только пользователь с ролью BUSINESS_OWNER")
    public BusinessCreateResponse createBusiness(@Valid @RequestBody BusinessInfoRequest businessInfo) {
        return businessService.createBusiness(businessInfo);
    }

}