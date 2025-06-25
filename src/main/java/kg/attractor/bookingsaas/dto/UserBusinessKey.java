package kg.attractor.bookingsaas.dto;

import kg.attractor.bookingsaas.dto.user.OutputUserDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserBusinessKey {
    private BusinessDto businessDto;
    private OutputUserDto user;
}
