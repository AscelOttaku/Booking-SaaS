package kg.attractor.bookingsaas.api;

import jakarta.validation.Valid;
import kg.attractor.bookingsaas.dto.ServiceDto;
import kg.attractor.bookingsaas.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/services")
@RequiredArgsConstructor
public class ServiceController {
    private final ServiceService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceDto createService(@Valid ServiceDto serviceDto) {
        return service.createService(serviceDto);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ServiceDto updateService(@Valid ServiceDto dto) {
        return service.updateService(dto);
    }

    @DeleteMapping("{serviceId}")
    @ResponseStatus(HttpStatus.OK)
    public ServiceDto deleteService(@PathVariable Long serviceId) {
        return service.deleteServiceById(serviceId);
    }
}
