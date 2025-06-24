package kg.attractor.bookingsaas.dto;

import jakarta.validation.Valid;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class CreateServiceDto {
    private String serviceName;
    private Long businessId;
    private List<@Valid BookDto> books;
}
