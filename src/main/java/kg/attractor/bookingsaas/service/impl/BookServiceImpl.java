package kg.attractor.bookingsaas.service.impl;

import kg.attractor.bookingsaas.dto.booked.BookDto;
import kg.attractor.bookingsaas.dto.PageHolder;
import kg.attractor.bookingsaas.dto.booked.BookHistoryDto;
import kg.attractor.bookingsaas.dto.mapper.impl.BookMapper;
import kg.attractor.bookingsaas.dto.mapper.impl.PageHolderWrapper;
import kg.attractor.bookingsaas.models.Book;
import kg.attractor.bookingsaas.repository.BookRepository;
import kg.attractor.bookingsaas.service.BookService;
import kg.attractor.bookingsaas.service.ScheduleService;
import kg.attractor.bookingsaas.service.ScheduleValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final PageHolderWrapper pageHolderWrapper;
    private final ScheduleValidator scheduleValidator;
    private final ScheduleService scheduleService;

    @Override
    public List<BookDto> findAllBooksByServiceId(Long serviceId) {
        Assert.notNull(serviceId, "serviceId must not be null");
        return bookRepository.findAllBooksByServicesId(serviceId)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public PageHolder<BookDto> findAllBooksByBusinessId(Long businessId, int page, int size) {
        Assert.notNull(businessId, "businessId must not be null");
        Pageable pageable = PageRequest.of(page, size, Sort.by("finishedAt").descending());
        Page<BookDto> bookPages = bookRepository.findAllBooksByBusinessId(businessId, pageable)
                .map(bookMapper::toDto);
        return pageHolderWrapper.wrapPageHolder(bookPages);
    }

    @Override
    public PageHolder<BookHistoryDto> findAlUsersBookedHistory(Long userId, int page, int size) {
        Assert.notNull(userId, "userId must not be null");
        Pageable pageable = PageRequest.of(page, size, Sort.by("finishedAt").descending());
        Page<BookHistoryDto> bookPages = bookRepository.findAllUsersBookedHistory(userId, pageable);
        return pageHolderWrapper.wrapPageHolder(bookPages);
    }

    @Override
    public BookDto createBook(BookDto bookDto) {
        Assert.notNull(bookDto, "bookDto must not be null");
        scheduleValidator.checkScheduleExistsById(bookDto.getScheduleId());
        long bookedCount = bookRepository.isBookAvailable(bookDto.getScheduleId(), bookDto.getStartedAt(), bookDto.getFinishedAt());
        long maxBookingSizeByScheduleId = scheduleService.findMaxBookingSizeByScheduleId(bookDto.getScheduleId());
        if (bookedCount >= maxBookingSizeByScheduleId) {
            throw new IllegalArgumentException("The schedule is fully booked for the selected time.");
        }

        Book book = bookMapper.toEntity(bookDto);
        return bookMapper.toDto(book);
    }
}
