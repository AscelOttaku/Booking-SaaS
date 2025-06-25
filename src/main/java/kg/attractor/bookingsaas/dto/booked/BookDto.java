package kg.attractor.bookingsaas.dto.booked;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import kg.attractor.bookingsaas.annotations.DurationBookTimes;
import kg.attractor.bookingsaas.markers.OnCreate;
import kg.attractor.bookingsaas.markers.OnUpdate;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@DurationBookTimes(durationInSeconds = 600, message = "Duration between startedAt and finishedAt should be more then 10 minutes")
public class BookDto {
    @Null(groups = OnCreate.class, message = "id must be null on create")
    @NotNull(groups = OnUpdate.class, message = "id must not be null on update")
    private Long id;

    @Schema(description = "Service ID", example = "2")
    @NotNull(groups = {OnCreate.class}, message = "serviceId must not be null")
    @Positive(groups = {OnCreate.class}, message = "serviceId must be positive")
    private Long scheduleId;

    @Schema(description = "Booking start time", example = "2024-07-01T10:00:00")
    @NotNull(groups = {OnCreate.class}, message = "startedAt must not be null")
    private LocalDateTime startedAt;

    @Schema(description = "Booking finish time", example = "2024-07-01T11:00:00")
    @NotNull(groups = {OnCreate.class}, message = "finishedAt must not be null")
    @Future(groups = {OnCreate.class}, message = "finishedAt must be in the future")
    private LocalDateTime finishedAt;
}