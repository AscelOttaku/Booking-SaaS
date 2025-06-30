package kg.attractor.bookingsaas.dto.user;

import kg.attractor.bookingsaas.enums.RoleEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OutputUserDto extends UserDto {
    private String email;
    private String imagePath;
    private String roleName;

    public OutputUserDto(String firstName, String middleName, String lastName, String phone, String email, String imagePath, RoleEnum roleName) {
        super.setFirstName(firstName);
        super.setMiddleName(middleName);
        super.setLastName(lastName);
        super.setPhone(phone);
        this.email = email;
        this.imagePath = imagePath;
        this.roleName = roleName.name();
    }
}
