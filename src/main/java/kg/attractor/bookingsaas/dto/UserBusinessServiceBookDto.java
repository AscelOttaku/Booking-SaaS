package kg.attractor.bookingsaas.dto;

import kg.attractor.bookingsaas.dto.booked.BookDto;
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
    private List<ServiceDto> services;
    private List<BookDto> bookDtos;
}
