package kg.attractor.bookingsaas.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import kg.attractor.bookingsaas.markers.OnCreate;
import kg.attractor.bookingsaas.markers.OnUpdate;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class BusinessDto {
    @Null(groups = OnCreate.class, message = "id must be null on create")
    @NotNull(groups = OnUpdate.class, message = "id must not be null on update")
    private Long id;

    @NotNull(groups = OnCreate.class, message = "user must not be null")
    private OutputUserDto user;

    @NotBlank(groups = OnCreate.class, message = "title must not be blank")
    @Size(max = 255, groups = {OnCreate.class, OnUpdate.class}, message = "title must be at most 255 characters")
    private String title;

    @Size(max = 2000, groups = {OnCreate.class, OnUpdate.class}, message = "description must be at most 2000 characters")
    private String description;

    @Null(groups = {OnCreate.class, OnUpdate.class}, message = "createdAt is set automatically")
    private LocalDateTime createdAt;

    @Null(groups = {OnCreate.class, OnUpdate.class}, message = "updatedAt is set automatically")
    private LocalDateTime updatedAt;

    private List<@Valid ServiceDto> services;
}
