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
public class UserBusinessBooksKey {
    private BusinessDto businessDto;
    private List<BookDto> bookDtos;
}
