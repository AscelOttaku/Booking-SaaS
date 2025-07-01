package kg.attractor.bookingsaas.api;

import kg.attractor.bookingsaas.dto.HolidayDto;
import kg.attractor.bookingsaas.service.HolidaysService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("holidays")
@RequiredArgsConstructor
public class HolidaysApi {
    private final WebClient webClient;
    private final HolidaysService holidaysService;

    @GetMapping("/{countryCode}/{year}")
    public Mono<List<HolidayDto>> getHolidays(@PathVariable String countryCode, @PathVariable int year) {
        return webClient.get()
                .uri("/{countryCode}/{year}", countryCode, year)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<HolidayDto>>() {});
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public HolidayDto createHoliday(@Validated @RequestBody HolidayDto holidayDto) {
        return holidaysService.createHoliday(holidayDto);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public HolidayDto updateHoliday(@Validated @RequestBody HolidayDto holidayDto) {
        return holidaysService.updateHoliday(holidayDto);
    }

    @DeleteMapping("/{name}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteHoliday(@PathVariable String name) {
        holidaysService.deleteHolidayByName(name);
    }
}
