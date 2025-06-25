package kg.attractor.bookingsaas.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.attractor.bookingsaas.dto.UserBusinessServiceBookDto;
import kg.attractor.bookingsaas.service.UserBusinessServiceBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Client Bookings", description = "APIs for managing client bookings")
@RestController
@RequestMapping("api/clients")
@RequiredArgsConstructor
public class UserBusinessServiceBookApi {
    private final UserBusinessServiceBookService userBusinessServiceBookService;

    @Operation(
            summary = "Get client bookings by business ID",
            description = "Retrieves a list of all client bookings for a specific business"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved client bookings"
    )
    @GetMapping("{businessId}")
    @ResponseStatus(HttpStatus.OK)
    public List<UserBusinessServiceBookDto> getUserBusinessServiceBooksByBusinessId(
            @Parameter(description = "ID of the business to retrieve bookings for", required = true, example = "1")
            @PathVariable Long businessId) {
        return userBusinessServiceBookService.getUserBusinessServiceBook(businessId);
    }
}
