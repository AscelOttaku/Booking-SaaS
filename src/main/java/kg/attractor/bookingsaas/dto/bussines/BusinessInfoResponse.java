package kg.attractor.bookingsaas.dto.bussines;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class BusinessInfoResponse {

    private String title;
    private String description;
    private LocalDateTime createdAt;
    private List<String> services;

}
