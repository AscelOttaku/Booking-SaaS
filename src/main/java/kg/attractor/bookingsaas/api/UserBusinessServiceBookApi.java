package kg.attractor.bookingsaas.api;

import kg.attractor.bookingsaas.dto.UserBusinessServiceBookDto;
import kg.attractor.bookingsaas.service.UserBusinessServiceBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/clients")
@RequiredArgsConstructor
public class UserBusinessServiceBookApi {
    private final UserBusinessServiceBookService userBusinessServiceBookService;

    @GetMapping("{businessId}")
    @ResponseStatus(HttpStatus.OK)
    public List<UserBusinessServiceBookDto> getUserBusinessServiceBookByBusinessId(@PathVariable Long businessId) {
        return userBusinessServiceBookService.getUserBusinessServiceBook(businessId);
    }
}
