package kg.attractor.bookingsaas.dto;

import kg.attractor.bookingsaas.dto.user.OutputUserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserBusinessServiceBookDto {
    private BusinessDto businessDto;
    private OutputUserDto user;
    private List<ServiceDto> services;
}
