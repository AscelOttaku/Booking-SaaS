package kg.attractor.bookingsaas.dto.impl;

import kg.attractor.bookingsaas.dto.CreateBookDto;
import kg.attractor.bookingsaas.models.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookMapper {

    public CreateBookDto toDto(Book book) {
        if (book == null) {
            return null;
        }
        CreateBookDto dto = new CreateBookDto();
        dto.setId(book.getId());
        dto.setStartedAt(book.getStartedAt());
        dto.setFinishedAt(book.getFinishedAt());
        return dto;
    }

    public Book toEntity(CreateBookDto dto) {
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
