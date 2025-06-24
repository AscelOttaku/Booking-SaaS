package kg.attractor.bookingsaas.api;

import jakarta.validation.Valid;
import kg.attractor.bookingsaas.dto.ServiceDto;
import kg.attractor.bookingsaas.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
}
