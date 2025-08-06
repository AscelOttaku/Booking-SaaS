package kg.attractor.bookingsaas.api;

import kg.attractor.bookingsaas.dto.HolidayDto;
import kg.attractor.bookingsaas.service.HolidaysService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("holidays")
@RequiredArgsConstructor
public class HolidaysApi {
    private final WebClient webClient;
    private final HolidaysService holidaysService;

    @GetMapping("/{countryCode}/{year}")
    public List<HolidayDto> getHolidays(@PathVariable String countryCode, @PathVariable int year) {
        return webClient.get()
                .uri("/{year}/{countryCode}", countryCode, year)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                            log.error("Client error while fetching holidays for country: {}, year: {}. Status: {}, Body: {}",
                                    countryCode, year, response.statusCode(), errorBody);
                            return Mono.error(new RuntimeException("Client error: " + response.statusCode()));
                        })
                )
                .bodyToMono(new ParameterizedTypeReference<List<HolidayDto>>() {})
                .blockOptional()
                .orElse(List.of());
    }

    @PreAuthorize("hasAnyAuthority('BUSINESS_OWNER','ADMIN')")
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public HolidayDto createHoliday(@Validated @RequestBody HolidayDto holidayDto) {
        return holidaysService.createHoliday(holidayDto);
    }

    @PreAuthorize("hasAnyAuthority('BUSINESS_OWNER','ADMIN')")
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public HolidayDto updateHoliday(@Validated @RequestBody HolidayDto holidayDto) {
        return holidaysService.updateHoliday(holidayDto);
    }

    @PreAuthorize("hasAnyAuthority('BUSINESS_OWNER','ADMIN')")
    @DeleteMapping("/{name}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteHoliday(@PathVariable String name) {
        holidaysService.deleteHolidayByName(name);
    }
}
