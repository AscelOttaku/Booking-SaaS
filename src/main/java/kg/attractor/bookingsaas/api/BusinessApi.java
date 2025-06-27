package kg.attractor.bookingsaas.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kg.attractor.bookingsaas.dto.BusinessDto;
import kg.attractor.bookingsaas.dto.PageHolder;
import kg.attractor.bookingsaas.dto.bussines.BusinessCreateResponse;
import kg.attractor.bookingsaas.dto.bussines.BusinessInfoRequest;
import kg.attractor.bookingsaas.dto.bussines.BusinessSummaryResponse;
import kg.attractor.bookingsaas.service.BusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/business")
@Tag(name = "Сервис Управления Бизнесом")
@RequiredArgsConstructor
public class BusinessApi {

    private final BusinessService businessService;

    @GetMapping
    @Operation(
            summary = "Получение списка бизнесов",
            description = "Получение списка всех доступных бизнесов")
    public PageHolder<BusinessSummaryResponse> getAllBusinesses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return businessService.getBusinessList(page, size);
    }

//    @GetMapping("/search")
//    @Operation(
//            summary = "Поиск бизнесов",
//            description = "Поиск бизнесов по названию")
//    public List<BusinessSummaryResponse> searchBusinesses(
//            @Parameter(description = "Название бизнеса для поиска")
//            @RequestParam(required = false) String name) {
//        return businessService.searchBusiness(name);
//    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Получение бизнеса по ID",
            description = "Получение полной информации о бизнесе по его ID")
    public BusinessDto getBusinessById(
            @Parameter(description = "ID бизнеса", required = true, example = "1")
            @PathVariable Long id) {
        return businessService.getBusinessById(id);
    }

//    @PreAuthorize("hasAuthority('BUSINESS_OWNER')")
//    @PostMapping
//    @Operation(
//            summary = "Создание бизнеса",
//            description = "Создание бизнеса с указанием названия и описания," +
//                    " создать бизнес сможет только пользователь с ролью BUSINESS_OWNER")
//    public BusinessCreateResponse createBusiness(@Valid @RequestBody BusinessInfoRequest businessInfo) {
//        return businessService.createBusiness(businessInfo);
//    }
}