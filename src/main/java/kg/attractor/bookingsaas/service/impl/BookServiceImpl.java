package kg.attractor.bookingsaas.service.impl;

import kg.attractor.bookingsaas.dto.PageHolder;
import kg.attractor.bookingsaas.dto.booked.BookDto;
import kg.attractor.bookingsaas.dto.booked.BookHistoryDto;
import kg.attractor.bookingsaas.dto.mapper.impl.BookMapper;
import kg.attractor.bookingsaas.dto.mapper.impl.PageHolderWrapper;
import kg.attractor.bookingsaas.models.Book;
import kg.attractor.bookingsaas.models.User;
import kg.attractor.bookingsaas.repository.BookRepository;
import kg.attractor.bookingsaas.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final PageHolderWrapper pageHolderWrapper;
    private final ScheduleValidator scheduleValidator;
    private final ScheduleService scheduleService;
    private final AuthorizedUserService authorizedUserService;
    private final HolidaysService holidaysService;

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
    public PageHolder<BookHistoryDto> findAlUsersBookedHistory(int page, int size) {
        Long authUserId = authorizedUserService.getAuthorizedUserId();
        Pageable pageable = PageRequest.of(page, size, Sort.by("finishedAt").descending());
        Page<BookHistoryDto> bookPages = bookRepository.findAllUsersBookedHistory(authUserId, pageable);
        return pageHolderWrapper.wrapPageHolder(bookPages);
    }

    @Override
    public BookDto createBook(BookDto bookDto) {
        Assert.notNull(bookDto, "bookDto must not be null");
        scheduleValidator.checkScheduleExistsById(bookDto.getScheduleId());

        // Check for Holiday conflicts
        var holidays = holidaysService.getHolidaysByYearFromDb(bookDto.getStartedAt().getYear());
        if (holidays.stream().anyMatch(holiday -> holiday.getDate().equals(bookDto.getStartedAt().toLocalDate()))) {
            throw new IllegalArgumentException("The booking date conflicts with a holiday.");
        }

        // Check for break conflicts
        LocalTime startedAtTime = bookDto.getStartedAt().toLocalTime();
        LocalTime finishedAtTime = bookDto.getFinishedAt().toLocalTime();
        boolean hasBreakConflicts = bookRepository.checkForBreakConflicts(bookDto.getScheduleId(), startedAtTime, finishedAtTime);
        if (hasBreakConflicts)
            throw new IllegalArgumentException("The booking time conflicts with a break period.");

        // Adding duration of breaks to the booking time
        int durationInMinutes = scheduleService.findDurationBetweenBooksByScheduleId(bookDto.getScheduleId());
        LocalDateTime startedAt = bookDto.getStartedAt().minusMinutes(durationInMinutes);
        LocalDateTime finishedAt = bookDto.getFinishedAt().plusMinutes(durationInMinutes);

        // Check if the book time is available
        long bookedCount = bookRepository.findBooksWithConflictTimesByScheduleId(bookDto.getScheduleId(), startedAt, finishedAt);
        long maxBookingSizeByScheduleId = scheduleService.findMaxBookingSizeByScheduleId(bookDto.getScheduleId());
        if (bookedCount >= maxBookingSizeByScheduleId) {
            throw new IllegalArgumentException("The schedule is fully booked for the selected time.");
        }

        Book book = bookMapper.toEntity(bookDto);
        User authUser = (User) authorizedUserService.getAuthUser();
        book.setUser(authUser);
        Book save = bookRepository.save(book);
        return bookMapper.toDto(save);
    }
}
