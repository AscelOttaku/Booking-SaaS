package kg.attractor.bookingsaas.service;

import kg.attractor.bookingsaas.dto.BookDto;

import java.util.List;

public interface BookService {
    List<BookDto> findAllBooksByServiceId(Long serviceId);

    List<BookDto> findAllBooksByBusinessId(Long businesId);
}
