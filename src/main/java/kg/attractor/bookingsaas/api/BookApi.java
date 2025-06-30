package kg.attractor.bookingsaas.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.attractor.bookingsaas.dto.PageHolder;
import kg.attractor.bookingsaas.dto.booked.BookDto;
import kg.attractor.bookingsaas.dto.booked.BookHistoryDto;
import kg.attractor.bookingsaas.markers.OnCreate;
import kg.attractor.bookingsaas.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Bookings", description = "APIs for managing bookings")
@RestController
@RequestMapping("api/booked")
@RequiredArgsConstructor
public class BookApi {
    private final BookService bookService;

    @Operation(
            summary = "Get bookings by business ID",
            description = "Retrieves a paginated list of all bookings for a specific business",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved bookings",
                            content = @Content(schema = @Schema(implementation = PageHolder.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Business not found"
                    )
            }
    )
    @GetMapping("business/{businessId}")
    @ResponseStatus(HttpStatus.OK)
    public PageHolder<BookDto> findAllBooksByBusinessId(
            @Parameter(description = "ID of the business to retrieve bookings for", required = true, example = "1")
            @PathVariable Long businessId,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        return bookService.findAllBooksByBusinessId(businessId, page, size);
    }

    @Operation(
            summary = "Get bookings by service ID",
            description = "Retrieves a paginated list of all bookings for a specific service",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved bookings",
                            content = @Content(schema = @Schema(implementation = PageHolder.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Service not found"
                    )
            }
    )
    @GetMapping("services/{serviceId}")
    @ResponseStatus(HttpStatus.OK)
    public PageHolder<BookDto> findAllBooksByServiceId(
            @Parameter(description = "ID of the service to retrieve bookings for", required = true, example = "1")
            @PathVariable Long serviceId,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        return bookService.findAllBooksByBusinessId(serviceId, page, size);
    }

    @Operation(
            summary = "Get user's booking history",
            description = "Retrieves a paginated list of a user's booking history",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved booking history",
                            content = @Content(schema = @Schema(implementation = PageHolder.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found"
                    )
            }
    )
    @GetMapping("clients/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public PageHolder<BookHistoryDto> findAlUsersBookedHistory(
            @Parameter(description = "ID of the user to retrieve booking history for", required = true, example = "1")
            @PathVariable Long userId,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        return bookService.findAlUsersBookedHistory(userId, page, size);
    }

    @PostMapping
    @Operation(
            summary = "Create a new booking",
            description = "Creates a new booking for a user",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Booking created successfully",
                            content = @Content(schema = @Schema(implementation = BookDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid booking data"
                    )
            }
    )
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto createBook(
            @Parameter(description = "Booking data to create", required = true)
            @RequestBody @Validated(OnCreate.class) BookDto bookDto) {
        return bookService.createBook(bookDto);
    }
}
