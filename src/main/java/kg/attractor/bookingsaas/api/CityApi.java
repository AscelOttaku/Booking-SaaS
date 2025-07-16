package kg.attractor.bookingsaas.api;

import kg.attractor.bookingsaas.dto.CityDto;
import kg.attractor.bookingsaas.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
@RequiredArgsConstructor
public class CityApi {
    private final CityService cityService;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<CityDto> findAllCities() {
        return cityService.findAllCities();
    }
}
