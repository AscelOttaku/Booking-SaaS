package kg.attractor.bookingsaas.api;

import jakarta.validation.Valid;
import kg.attractor.bookingsaas.dto.PageHolder;
import kg.attractor.bookingsaas.dto.user.OutputUserDto;
import kg.attractor.bookingsaas.dto.user.UpdateUserDto;
import kg.attractor.bookingsaas.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasAnyAuthority('CLIENT','ADMIN')")
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public UpdateUserDto updateUser(@Valid @ModelAttribute UpdateUserDto updateUserDto) throws IOException {
        return userService.updateUser(updateUserDto);
    }

    @PreAuthorize("hasAnyAuthority('CLIENT','ADMIN')")
    @GetMapping
    public String greetUser() {
        return "Swagger is cool";
    }

    @PreAuthorize("hasAnyAuthority('BUSINESS_OWNER')")
    @GetMapping("/business/{businessTitle}")
    @ResponseStatus(HttpStatus.OK)
    public PageHolder<OutputUserDto> findAllClientsByBusinessTitle(
            @PathVariable String businessTitle,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return userService.getUsersByBusinessTitle(businessTitle, page, size);
    }

    @PreAuthorize("hasAnyAuthority('BUSINESS_OWNER')")
    @GetMapping("/service/clients")
    @ResponseStatus(HttpStatus.OK)
    public PageHolder<OutputUserDto> findAllClientsByServiceId(
            @RequestParam Long serviceId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return userService.findUsersByServiceId(serviceId, page, size);
    }
}
