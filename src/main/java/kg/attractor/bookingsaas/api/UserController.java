package kg.attractor.bookingsaas.api;

import jakarta.validation.Valid;
import kg.attractor.bookingsaas.dto.UpdateUserDto;
import kg.attractor.bookingsaas.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("users")
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
    public String greetUser(){
        return "SWagger is cool";
    }
}
