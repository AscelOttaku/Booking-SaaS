package kg.attractor.bookingsaas.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.attractor.bookingsaas.dto.BusinessDto;
import kg.attractor.bookingsaas.dto.PageHolder;
import kg.attractor.bookingsaas.service.BusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @GetMapping("most-five-popular/{businessTitle}")
    @Operation(
            summary = "Получение пяти самых популярных бизнесов по названию",
            description = "Получение списка из пяти самых популярных бизнесов по заданному названию")
    @ResponseStatus(HttpStatus.OK)
    public List<BusinessDto> findMostFivePopularBusinesses(
            @Parameter(description = "Название бизнеса", required = true, example = "Coffee Shop")
            @PathVariable String businessTitle
    ) {
        return businessService.findMostPopularFiveBusinessesByBusinessTitleContatining(businessTitle);
    }
}