package kg.attractor.bookingsaas.dto;

import kg.attractor.bookingsaas.dto.booked.BookDto;
import kg.attractor.bookingsaas.dto.user.OutputUserDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class UserBusinessKey {
    private BusinessDto businessDto;
    private OutputUserDto userDto;
    private List<BookDto> bookDtos;
}
