package kg.attractor.bookingsaas.service;

import kg.attractor.bookingsaas.dto.PageHolder;
import kg.attractor.bookingsaas.dto.booked.BookDto;
import kg.attractor.bookingsaas.dto.booked.BookInfoDto;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface BookService {
    PageHolder<BookDto> findAllBooksByServiceId(Long serviceId, int page, int size);

    PageHolder<BookDto> findAllBooksByBusinessTitle(String businessTitle, int page, int size);

    PageHolder<BookInfoDto> findAlUsersBookedHistory(int page, int size);

    BookInfoDto findUserHistory();

    BookInfoDto findUserHistoryByUserId(Long userId);

    BookDto createBook(BookDto bookDto);

    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    BookDto updateBook(BookDto bookDto);

    BookDto cancelBook(Long bookId);

    BookInfoDto findBookById(Long bookId);
}
