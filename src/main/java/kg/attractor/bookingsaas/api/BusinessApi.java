package kg.attractor.bookingsaas.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.attractor.bookingsaas.dto.BusinessDto;
import kg.attractor.bookingsaas.dto.PageHolder;
import kg.attractor.bookingsaas.service.BusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public PageHolder<BusinessDto> getAllBusinesses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return businessService.getBusinessList(page, size);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Получение бизнеса по ID",
            description = "Получение полной информации о бизнесе по его ID")
    public BusinessDto getBusinessById(
            @Parameter(description = "ID бизнеса", required = true, example = "1")
            @PathVariable Long id) {
        return businessService.getBusinessById(id);
    }
}