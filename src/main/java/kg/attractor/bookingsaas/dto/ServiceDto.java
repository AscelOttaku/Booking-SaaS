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

    @NotNull(groups = OnCreate.class, message = "Duration must not be null")
    @Min(groups = OnCreate.class, value = 7, message = "Duration must be at least 7 minutes")
    private Integer duration;

    @NotNull(groups = OnCreate.class, message = "Price must not be null")
    @Positive(groups = OnCreate.class, message = "Price must be a positive number")
    private BigDecimal price;
}
