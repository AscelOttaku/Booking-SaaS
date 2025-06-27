package kg.attractor.bookingsaas.service;

import kg.attractor.bookingsaas.dto.booked.BookDto;
import kg.attractor.bookingsaas.dto.PageHolder;
import kg.attractor.bookingsaas.dto.booked.BookHistoryDto;

import java.util.List;

public interface BookService {
    List<BookDto> findAllBooksByServiceId(Long serviceId);

    PageHolder<BookDto> findAllBooksByBusinessId(Long businesId, int page, int size);

    PageHolder<BookHistoryDto> findAlUsersBookedHistory(Long userId, int page, int size);

    BookDto createBook(BookDto bookDto);
}
