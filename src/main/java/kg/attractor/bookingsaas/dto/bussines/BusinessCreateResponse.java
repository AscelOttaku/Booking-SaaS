package kg.attractor.bookingsaas.dto.bussines;

import lombok.Builder;
import lombok.Data;

@Data
public class BusinessCreateResponse {
    private Long id;
    private String title;
    private String description;
}
