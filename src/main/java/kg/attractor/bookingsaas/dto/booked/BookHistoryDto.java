package kg.attractor.bookingsaas.dto.booked;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookHistoryDto {
    public BookHistoryDto(Long id, String serviceName, String businessName, LocalDateTime startedAt, LocalDateTime finishedAt) {
        this.id = id;
        this.serviceName = serviceName;
        this.businessName = businessName;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
    }

    @Schema(description = "Booking ID", example = "1")
    private Long id;

    @Schema(description = "Service name", example = "Haircut")
    private String serviceName;

    @Schema(description = "Business name", example = "Beauty Salon")
    private String businessName;

    @Schema(description = "Booking start time", example = "2024-07-01T10:00:00")
    private LocalDateTime startedAt;

    @Schema(description = "Booking finish time", example = "2024-07-01T11:00:00")
    private LocalDateTime finishedAt;
}
