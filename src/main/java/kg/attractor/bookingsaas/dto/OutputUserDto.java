package kg.attractor.bookingsaas.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class OutputUserDto {
    private String firstName;
    private String middleName;
    private String lastName;
    private String phone;
    private LocalDate birthday;
    private String email;
    private String imagePath;
    private String roleName;
}
