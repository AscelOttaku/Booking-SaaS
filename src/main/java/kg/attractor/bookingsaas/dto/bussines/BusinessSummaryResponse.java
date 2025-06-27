package kg.attractor.bookingsaas.dto.bussines;

import lombok.Data;

import java.util.List;

@Data
public class BusinessSummaryResponse {
    private Long id;
    private String title;
    private String description;
    private String address;
    private String logo;
    private String city;
    private String businessCategory;
    private List<String> businessUnderCategory;
}
