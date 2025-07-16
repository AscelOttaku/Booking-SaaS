package kg.attractor.bookingsaas.dto.booked;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import kg.attractor.bookingsaas.dto.user.OutputUserDto;
import kg.attractor.bookingsaas.markers.OnCreate;
import kg.attractor.bookingsaas.markers.OnUpdate;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    @Null(groups = OnCreate.class, message = "id must be null on create")
    @NotNull(groups = OnUpdate.class, message = "id must not be null on update")
    private Long id;

    @Schema(description = "Service ID", example = "2")
    @NotNull(groups = {OnCreate.class}, message = "serviceId must not be null")
    @Positive(groups = {OnCreate.class}, message = "serviceId must be positive")
    private Long scheduleId;

    @Schema(description = "Booking start time", example = "2024-07-01 10:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(groups = {OnCreate.class}, message = "startedAt must not be null")
    @Future(groups = {OnCreate.class}, message = "startedAt must be in the future")
    private LocalDateTime startedAt;

    @Schema(description = "Booking end time", accessMode = Schema.AccessMode.READ_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Null(groups = {OnCreate.class, OnUpdate.class}, message = "finishedAt must be null on create or update")
    private LocalDateTime finishedAt;

    @Schema(description = "Booking status", accessMode = Schema.AccessMode.READ_ONLY)
    private OutputUserDto userDto;
}