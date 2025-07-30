package kg.attractor.bookingsaas.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import kg.attractor.bookingsaas.annotations.BusinessUniqueTitle;
import kg.attractor.bookingsaas.dto.user.OutputUserDto;
import kg.attractor.bookingsaas.markers.OnCreate;
import kg.attractor.bookingsaas.markers.OnUpdate;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BusinessDto {
    @Null(groups = OnCreate.class, message = "id must be null on create")
    @NotNull(groups = OnUpdate.class, message = "id must not be null on update")
    private Long id;

    @Null(groups = {OnCreate.class, OnUpdate.class}, message = "user must not be null")
    private OutputUserDto user;

    @NotBlank(groups = OnCreate.class, message = "title must not be blank")
    @Size(max = 255, groups = {OnCreate.class, OnUpdate.class}, message = "title must be at most 255 characters")
    @BusinessUniqueTitle(message = "title must be unique", groups = {OnCreate.class, OnUpdate.class})
    private String title;

    @Size(max = 2000, groups = {OnCreate.class, OnUpdate.class}, message = "description must be at most 2000 characters")
    private String description;

    @Null(groups = {OnCreate.class, OnUpdate.class}, message = "createdAt is set automatically")
    private LocalDateTime createdAt;

    @Null(groups = {OnCreate.class, OnUpdate.class}, message = "updatedAt is set automatically")
    private LocalDateTime updatedAt;

    private List<@Valid ServiceDto> services;

    public String getCreatedFormat() {
        return DateTimeFormatter.ofPattern("dd:MM:yyyy")
                .format(createdAt);
    }

    public String getUpdatedFormat() {
        return updatedAt != null ? DateTimeFormatter.ofPattern("dd:MM:yyyy")
                .format(updatedAt) : "No updated";
    }
}
