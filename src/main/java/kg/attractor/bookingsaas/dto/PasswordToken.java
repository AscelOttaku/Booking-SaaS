package kg.attractor.bookingsaas.dto;

import jakarta.validation.constraints.NotBlank;
import kg.attractor.bookingsaas.annotations.ValidUserPassword;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PasswordToken {
    private String token;

    @NotBlank
    @ValidUserPassword
    private String password;
}
