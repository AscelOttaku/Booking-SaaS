package kg.attractor.bookingsaas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import kg.attractor.bookingsaas.annotations.UniqueHolidayName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@UniqueHolidayName(message = "Holiday with this name already exists")
public class HolidayDto {
    private Long id;

    @NotNull(message = "Date must not be null")
    private LocalDate date;
    private String localName;

    @NotBlank(message = "Name must not be blank")
    private String name;

    @NotBlank(message = "Country code must not be blank")
    private String countryCode;

    private Boolean global;

    @NotNull(message = "Types list must not be null")
    @NotEmpty(message = "Types list must not be empty")
    private List<String> types;
}