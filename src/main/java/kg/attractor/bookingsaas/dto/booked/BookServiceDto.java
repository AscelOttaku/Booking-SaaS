package kg.attractor.bookingsaas.dto.booked;

import kg.attractor.bookingsaas.dto.ServiceDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class BookServiceDto {
    private ServiceDto serviceDto;
    private List<BookDto> bookDtos;
}
