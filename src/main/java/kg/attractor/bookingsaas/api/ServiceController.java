package kg.attractor.bookingsaas.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import kg.attractor.bookingsaas.dto.PageHolder;
import kg.attractor.bookingsaas.dto.ServiceDto;
import kg.attractor.bookingsaas.dto.booked.BookServiceDto;
import kg.attractor.bookingsaas.markers.OnCreate;
import kg.attractor.bookingsaas.markers.OnUpdate;
import kg.attractor.bookingsaas.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/services")
@RequiredArgsConstructor
public class ServiceController {
    private final ServiceService service;

    @Operation(
            summary = "Create a new service",
            description = "Creates a new service with the provided details."
    )
    @ApiResponse(responseCode = "201", description = "Service created successfully",
            content = @Content(schema = @Schema(implementation = ServiceDto.class)))
    @ApiResponse(responseCode = "400", description = "Invalid input data or illegal argument",
            content = @Content)
    @ApiResponse(responseCode = "404", description = "Service not found (NoSuchElementException)",
            content = @Content)
    @PreAuthorize("hasAnyAuthority('BUSINESS_OWNER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceDto createService(@Validated(OnCreate.class) @RequestBody ServiceDto serviceDto) {
        return service.createService(serviceDto);
    }

    @Operation(
            summary = "Update an existing service",
            description = "Updates the details of an existing service."
    )
    @ApiResponse(responseCode = "200", description = "Service updated successfully",
            content = @Content(schema = @Schema(implementation = ServiceDto.class)))
    @ApiResponse(responseCode = "400", description = "Invalid input data or illegal argument",
            content = @Content)
    @ApiResponse(responseCode = "404", description = "Service not found (NoSuchElementException)",
            content = @Content)
    @PreAuthorize("hasAnyAuthority('BUSINESS_OWNER')")
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ServiceDto updateService(@Validated(OnUpdate.class) @RequestBody ServiceDto dto) {
        return service.updateService(dto);
    }

    @Operation(
            summary = "Delete a service by ID",
            description = "Deletes the service with the specified ID."
    )
    @ApiResponse(responseCode = "200", description = "Service deleted successfully",
            content = @Content(schema = @Schema(implementation = ServiceDto.class)))
    @ApiResponse(responseCode = "404", description = "Service not found (NoSuchElementException)",
            content = @Content)
    @ApiResponse(responseCode = "400", description = "Invalid service ID or illegal argument",
            content = @Content)
    @PreAuthorize("hasAnyAuthority('BUSINESS_OWNER')")
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public ServiceDto deleteService(@RequestParam Long serviceId) {
        return service.deleteServiceById(serviceId);
    }

    @Operation(
            summary = "Get all services by business title",
            description = "Retrieves a paginated list of services for the specified business title."
    )
    @ApiResponse(responseCode = "200", description = "List of services retrieved successfully",
            content = @Content(schema = @Schema(implementation = PageHolder.class)))
    @ApiResponse(responseCode = "404", description = "Business not found (NoSuchElementException)",
            content = @Content)
    @GetMapping("/business/{businessTitle}")
    @ResponseStatus(HttpStatus.OK)
    public PageHolder<ServiceDto> findAllServicesByBusinessTitle(
            @PathVariable String businessTitle,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return service.findAllServicesByBusinessTitle(businessTitle, page, size);
    }

    @Operation(
            summary = "Get clients by service ID",
            description = "Returns a paginated list of clients who have booked the specified service."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of clients returned"),
            @ApiResponse(responseCode = "404", description = "Service not found")
    })
    @GetMapping("/{serviceId}/clients")
    public PageHolder<BookServiceDto> findClientsByServiceId(
            @Parameter(description = "ID of the service to fetch clients for", required = true)
            @PathVariable Long serviceId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return service.findServiceAndBooksByServiceId(serviceId, page, size);
    }

    @GetMapping("/most-popular/{businessTitle}")
    @ResponseStatus(HttpStatus.OK)
    public ServiceDto findMostPopularServiceByBusinessTitle(@PathVariable String businessTitle) {
        return service.findMostPopularServiceByBusinessTitle(businessTitle);
    }

    @Operation(
            summary = "Get services sorted by popularity for a business",
            description = "Returns a list of services sorted by their popularity for the specified business title."
    )
    @GetMapping("/sorted-popular/{businessTitle}")
    @ResponseStatus(HttpStatus.OK)
    public List<ServiceDto> findServicesSortedByPopularityByBusinessTitle(
            @Parameter(description = "Title of the business to fetch services for", required = true)
            @PathVariable String businessTitle
    ) {
        return service.findServicesSortedByPopularityByBusinessTitle(businessTitle);
    }
}