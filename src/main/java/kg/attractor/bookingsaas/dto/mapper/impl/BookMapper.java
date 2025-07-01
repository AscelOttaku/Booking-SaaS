package kg.attractor.bookingsaas.dto.mapper.impl;

import kg.attractor.bookingsaas.dto.booked.BookDto;
import kg.attractor.bookingsaas.dto.mapper.OutputUserMapper;
import kg.attractor.bookingsaas.models.Book;
import kg.attractor.bookingsaas.models.Schedule;
import kg.attractor.bookingsaas.projection.BookInfo;
import kg.attractor.bookingsaas.projection.UserBusinessServiceProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookMapper {
    private final OutputUserMapper outputUserMapper;

    public BookDto toDto(Book book) {
        if (book == null) {
            return null;
        }
        BookDto dto = new BookDto();
        dto.setId(book.getId());
        dto.setStartedAt(book.getStartedAt());
        dto.setScheduleId(book.getSchedule().getId());
        dto.setUserDto(outputUserMapper.mapToDto(book.getUser()));
        return dto;
    }

    public BookDto toDto(BookInfo book) {
        if (book == null) {
            return null;
        }
        BookDto dto = new BookDto();
        dto.setId(book.getId());
        dto.setStartedAt(book.getStartedAt());
        dto.setScheduleId(book.getSchedule().getId());
        dto.setUserDto(outputUserMapper.mapToDto(book.getUser()));
        return dto;
    }

    public Book toEntity(BookDto dto) {
        if (dto == null) {
            return null;
        }
        Book book = new Book();
        book.setStartedAt(dto.getStartedAt());

        Schedule schedule = new Schedule();
        schedule.setId(dto.getScheduleId());
        book.setSchedule(schedule);
        book.setUser(outputUserMapper.mapToEntity(dto.getUserDto()));
        return book;
    }
}