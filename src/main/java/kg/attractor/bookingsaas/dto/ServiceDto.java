package kg.attractor.bookingsaas.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import kg.attractor.bookingsaas.markers.OnCreate;
import kg.attractor.bookingsaas.markers.OnUpdate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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

    private List<@Valid BookDto> books;
}