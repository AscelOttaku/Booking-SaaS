package kg.attractor.bookingsaas.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OutputUserDto extends UserDto{
    private String email;
    private String imagePath;
    private String roleName;
}
