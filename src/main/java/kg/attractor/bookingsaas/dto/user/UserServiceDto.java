package kg.attractor.bookingsaas.dto.user;

import kg.attractor.bookingsaas.dto.ServiceDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserServiceDto {
    private ServiceDto serviceDto;
    private OutputUserDto outputUserDto;
}
