package kg.attractor.bookingsaas.dto;

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
