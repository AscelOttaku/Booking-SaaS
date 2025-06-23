package kg.attractor.bookingsaas.dto.auth;

import kg.attractor.bookingsaas.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String token;
    private String username;
    private RoleEnum role;
}
