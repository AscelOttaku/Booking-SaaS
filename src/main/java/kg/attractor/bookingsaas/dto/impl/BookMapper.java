package kg.attractor.bookingsaas.dto.impl;

import kg.attractor.bookingsaas.dto.BookDto;
import kg.attractor.bookingsaas.models.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookMapper {

    public BookDto toDto(Book book) {
        if (book == null) {
            return null;
        }
        BookDto dto = new BookDto();
        dto.setId(book.getId());
        dto.setStartedAt(book.getStartedAt());
        dto.setFinishedAt(book.getFinishedAt());
        return dto;
    }

    public Book toEntity(BookDto dto) {
        if (dto == null) {
            return null;
        }
        Book book = new Book();
        book.setId(dto.getId());
        dto.setStartedAt(book.getStartedAt());
        dto.setFinishedAt(book.getFinishedAt());
        return book;
    }
}
