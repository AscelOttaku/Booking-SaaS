package kg.attractor.bookingsaas.service.impl;

import kg.attractor.bookingsaas.dto.PageHolder;
import kg.attractor.bookingsaas.dto.booked.BookDto;
import kg.attractor.bookingsaas.dto.booked.BookInfoDto;
import kg.attractor.bookingsaas.dto.mapper.impl.BookMapper;
import kg.attractor.bookingsaas.dto.mapper.impl.PageHolderWrapper;
import kg.attractor.bookingsaas.enums.BookStatus;
import kg.attractor.bookingsaas.event.BookCanceledEvent;
import kg.attractor.bookingsaas.event.BookCreateEvent;
import kg.attractor.bookingsaas.event.publisher.BookEventPublisher;
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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

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
    private final ServiceDurationProvider serviceDurationProvider;
    private final BookEventPublisher bookEventPublisher;

    @Override
    public PageHolder<BookDto> findAllBooksByServiceId(Long serviceId, int page, int size) {
        Assert.notNull(serviceId, "serviceId must not be null");
        Pageable pageable = PageRequest.of(page, size, Sort.by("finishedAt").descending());
        Page<BookDto> bookPages = bookRepository.findAllBooksByServicesId(serviceId, pageable)
                .map(bookMapper::toDto);
        return pageHolderWrapper.wrapPageHolder(bookPages);
    }

    @Override
    public PageHolder<BookDto> findAllBooksByBusinessTitle(String businessTitle, int page, int size) {
        Assert.notNull(businessTitle, "businessTitle must not be null");
        Pageable pageable = PageRequest.of(page, size, Sort.by("startedAt").descending());
        Page<BookDto> bookPages = bookRepository.findAllBooksByBusinessTitle(businessTitle, pageable)
                .map(bookMapper::toDto);
        return pageHolderWrapper.wrapPageHolder(bookPages);
    }

    @Override
    public PageHolder<BookInfoDto> findAlUsersBookedHistory(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("startedAt").descending());
        Page<BookInfoDto> bookPages = bookRepository.findAllUsersBookedHistory(pageable);
        return pageHolderWrapper.wrapPageHolder(bookPages);
    }

    @Override
    public List<BookInfoDto> findUserHistory() {
        Long authUserId = authorizedUserService.getAuthorizedUserId();
        return bookRepository.findUserHistoryById(authUserId);
    }

    @Override
    public List<BookInfoDto> findUserHistoryByUserId(Long userId) {
        return bookRepository.findUserHistoryById(userId);
    }

    @Override
    public BookDto createBook(BookDto bookDto) {
        Assert.notNull(bookDto, "bookDto must not be null");
        scheduleValidator.checkScheduleExistsById(bookDto.getScheduleId());

        validateForBreakHolidayAndWorkTimeConflicts(bookDto);

        Book book = bookMapper.toEntity(bookDto);
        User authUser = (User) authorizedUserService.getAuthUser();
        book.setUser(authUser);
        book.setFinishedAt(calculateFinishedAt(bookDto));
        Book save = bookRepository.save(book);

        bookEventPublisher.publishBookCreateEvent(new BookCreateEvent(save.getId(), authUser.getEmail()));
        return bookMapper.toDto(save);
    }

    private LocalDateTime calculateFinishedAt(BookDto bookDto) {
        int serviceDuration = serviceDurationProvider.findServiceDurationByScheduleId(bookDto.getScheduleId());
        return bookDto.getStartedAt().plusMinutes(serviceDuration);
    }

    private void validateForBreakHolidayAndWorkTimeConflicts(BookDto bookDto) {
        LocalDateTime startDateTime = bookDto.getStartedAt();
        long scheduleId = bookDto.getScheduleId();
        int serviceDuration = serviceDurationProvider.findServiceDurationByScheduleId(scheduleId);
        int betweenBooksDuration = scheduleService.findDurationBetweenBooksByScheduleId(scheduleId);

        LocalDate bookingDate = startDateTime.toLocalDate();
        LocalTime startTime = startDateTime.toLocalTime();
        LocalTime endTime = startTime.plusMinutes(serviceDuration);

        // 1. Holiday check
        boolean isHoliday = holidaysService.getHolidaysByYearFromDb(bookingDate.getYear())
                .stream()
                .anyMatch(holiday -> holiday.getDate().equals(bookingDate));
        if (isHoliday) {
            throw new IllegalArgumentException("The booking date conflicts with a holiday.");
        }

        // 2. Work time conflict
        LocalTime startAt = startTime.minusMinutes(serviceDuration);
        LocalTime endAt = endTime.plusMinutes(serviceDuration);
        if (scheduleValidator.checkForWorkTimeConflicts(scheduleId, startAt, endAt)) {
            throw new IllegalArgumentException("The booking time conflicts with the work schedule.");
        }

        // 3. Break conflict
        if (bookRepository.checkForBreakConflicts(scheduleId, startTime, endTime)) {
            throw new IllegalArgumentException("The booking time conflicts with a break period.");
        }

        // 4. Overbooking check
        LocalDateTime conflictStart = startDateTime.minusMinutes(betweenBooksDuration);
        LocalDateTime conflictEnd = startDateTime.plusMinutes(serviceDuration + (long) betweenBooksDuration);
        long existingBookings = bookRepository.findBooksWithConflictTimesByScheduleId(scheduleId, conflictStart, conflictEnd);
        long maxAllowed = scheduleService.findMaxBookingSizeByScheduleId(scheduleId);
        if (existingBookings >= maxAllowed) {
            throw new IllegalArgumentException("The schedule is fully booked for the selected time.");
        }
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    @Override
    public BookDto updateBook(BookDto bookDto) {
        var existingBook = bookRepository.findById(bookDto.getId())
                .orElseThrow(() -> new NoSuchElementException("Book with ID " + bookDto.getId() + " does not exist"));

        if (!Objects.equals(existingBook.getUser().getId(), authorizedUserService.getAuthorizedUserId()))
            throw new IllegalArgumentException("You do not have permission to update this book");

        bookDto.setScheduleId(existingBook.getSchedule().getId());

        validateForBreakHolidayAndWorkTimeConflicts(bookDto);

        existingBook.setStartedAt(bookDto.getStartedAt());
        existingBook.setFinishedAt(calculateFinishedAt(bookDto));
        return bookMapper.toDto(bookRepository.save(existingBook));
    }

    @Override
    public BookDto cancelBook(Long bookId) {
        Assert.isTrue(bookId != null && bookId > 0, "bookId must not be null and positive");
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NoSuchElementException("Book with ID " + bookId + " does not exist"));

        User authUser = (User) authorizedUserService.getAuthUser();
        if (!Objects.equals(book.getUser().getId(), authUser.getId()))
            throw new IllegalArgumentException("You do not have permission to cancel this book");

        int durationInMinutes = Duration.between(book.getStartedAt(), LocalDateTime.now()).toMinutesPart();
        if (durationInMinutes < 30) {
            throw new IllegalArgumentException("You cannot cancel a booking less than 30 minutes before it starts.");
        }
        book.setStatus(BookStatus.CANCELED);

        bookEventPublisher.publishBookCancelEvent(new BookCanceledEvent(bookId, authUser.getEmail(), LocalDateTime.now()));
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public BookInfoDto findBookById(Long bookId) {
        return bookRepository.findBookInfoById(bookId)
                .orElseThrow(() -> new NoSuchElementException("Book with ID " + bookId + " does not exist"));
    }
}
