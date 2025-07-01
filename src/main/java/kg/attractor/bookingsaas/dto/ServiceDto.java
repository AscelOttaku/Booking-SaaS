package kg.attractor.bookingsaas.dto;

import jakarta.validation.constraints.*;
import kg.attractor.bookingsaas.markers.OnCreate;
import kg.attractor.bookingsaas.markers.OnUpdate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class ServiceDto {
    @Null(groups = OnCreate.class, message = "ID must not be null for update")
    @NotNull(groups = OnUpdate.class, message = "ID must not be null on update")
    private Long id;

    @NotBlank(groups = OnCreate.class, message = "Service name must not be blank")
    private String serviceName;

    @NotNull(groups = OnCreate.class, message = "Business ID must not be null")
    private Long businessId;

    @NotNull(groups = OnCreate.class, message = "Duration in minutes must not be null")
    @Min(value = 7, groups = OnCreate.class, message = "Duration in minutes must be at least 7 minutes")
    @Max(value = 1440, groups = OnCreate.class, message = "Duration in minutes must not exceed 24 hours")
    private Integer durationInMinutes;

    @NotNull(groups = OnCreate.class, message = "Price must not be null")
    @Positive(groups = OnCreate.class, message = "Price must be a positive number")
    private BigDecimal price;
}
