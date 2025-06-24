package kg.attractor.bookingsaas.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class OutputUserDto extends UserDto{
    private String email;
    private String imagePath;
    private String roleName;
}
