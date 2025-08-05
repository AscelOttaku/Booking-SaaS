package kg.attractor.bookingsaas.module.repository;

import kg.attractor.bookingsaas.dto.booked.BookInfoDto;
import kg.attractor.bookingsaas.enums.RoleEnum;
import kg.attractor.bookingsaas.models.*;
import kg.attractor.bookingsaas.repository.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ActiveProfiles("test")
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private DayOfWeekRepository dayOfWeekRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private BreakPeriodRepository breakPeriodRepository;

    private Service service;
    private Schedule schedule;
    private User user;

    @BeforeEach
    void setUp() {
        // Create and save Role
        Role role = new Role();
        role.setRoleName(RoleEnum.CLIENT);
        role = roleRepository.save(role);

        // Create and save User
        user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setMiddleName("M");
        user.setEmail("john.doe@example.com");
        user.setPhone("1234567890");
        user.setPassword("password");
        user.setRole(role);
        user.setBirthday(LocalDate.of(1990, 1, 1));
        user = userRepository.save(user);

        // Create and save City
        City city = new City();
        city.setName("Test City");
        city = cityRepository.save(city);

        // Create and save BusinessCategory
        BusinessCategory category = new BusinessCategory();
        category.setName("Some Category");

        // Create and save Business
        Business business = new Business();
        business.setTitle("Test Business");
        business.setBusinessCategory(category);
        business.setCity(city);
        business.setUser(user);
        business = businessRepository.save(business);

        // Create and save Service
        service = new Service();
        service.setServiceName("Test Service");
        service.setBusiness(business);
        service.setDurationInMinutes(30);
        service.setPrice(BigDecimal.valueOf(100));
        service = serviceRepository.save(service);

        // Create and save DayOfWeek
        DayOfWeek dayOfWeek = new DayOfWeek();
        dayOfWeek.setName("Monday");
        dayOfWeek.setIsWorking(true);
        dayOfWeek = dayOfWeekRepository.save(dayOfWeek);

        // Create and save Schedule
        schedule = new Schedule();
        schedule.setService(service);
        schedule.setDayOfWeek(dayOfWeek);
        schedule.setStartTime(LocalTime.of(9, 0));
        schedule.setEndTime(LocalTime.of(18, 0));
        schedule.setIsAvailable(true);
        schedule.setMaxBookingSize(10);
        schedule = scheduleRepository.save(schedule);

        // Create and save several Books
        for (int i = 0; i < 5; i++) {
            Book book = new Book();
            book.setUser(user);
            book.setSchedule(schedule);
            book.setStartedAt(LocalDateTime.now().minusHours(i + 2));
            book.setFinishedAt(LocalDateTime.now().minusHours(i + 1));
            bookRepository.save(book);
        }
    }

    @Test
    void findAllBooksByServicesId() {
        // Given
        Long serviceId = service.getId();

        // When
        Pageable pageable = PageRequest.of(0, 10, Sort.by("finishedAt").descending());
        Page<Book> books = bookRepository.findAllBooksByServicesId(serviceId, pageable);

        // Then
        Assertions.assertThat(books)
                .isNotNull()
                .isNotEmpty()
                .hasSize(5)
                .extracting(Book::getSchedule)
                .isNotNull()
                .allMatch(oneSchedule -> oneSchedule.getService().getId().equals(serviceId));
    }

    @Test
    void findAllBooksByBusinessTitle() {
        // Given
        String businessTitle = "Test Business";

        // When
        Pageable pageable = PageRequest.of(0, 10, Sort.by("finishedAt").descending());
        Page<Book> books = bookRepository.findAllBooksByBusinessTitle(businessTitle, pageable);

        // Then
        Assertions.assertThat(books)
                .isNotNull()
                .isNotEmpty()
                .hasSize(5)
                .extracting(Book::getSchedule)
                .isNotNull()
                .extracting(Schedule::getService)
                .isNotNull()
                .allMatch(temporalService -> temporalService.getBusiness().getTitle().equals(businessTitle));
    }

    @Test
    void findAllUsersBookedHistory() {
        // When
        Pageable pageable = PageRequest.of(0, 10, Sort.by("finishedAt").descending());
        Page<BookInfoDto> userBookedHistory = bookRepository.findAllUsersBookedHistory(pageable);

        // Then
        Assertions.assertThat(userBookedHistory)
                .isNotNull()
                .isNotEmpty()
                .hasSize(5)
                .extracting(
                        bookInfoDto -> bookInfoDto.getOutputUserDto().getEmail(),
                        BookInfoDto::getServiceName,
                        BookInfoDto::getBusinessName
                )
                .allSatisfy(tuple -> {
                    Object[] values = tuple.toArray();

                    String actualEmail = (String) values[0];
                    String actualServiceName = (String) values[1];
                    String actualBusinessTitle = (String) values[2];

                    String expectedEmail = user.getEmail();
                    String expectedServiceName = service.getServiceName();
                    String expectedBusinessTitle = service.getBusiness().getTitle();

                    Assertions.assertThat(actualEmail)
                            .as("User email")
                            .isNotNull()
                            .isEqualTo(expectedEmail);

                    Assertions.assertThat(actualServiceName)
                            .as("Service name")
                            .isNotNull()
                            .isEqualTo(expectedServiceName);

                    Assertions.assertThat(actualBusinessTitle)
                            .as("Business title")
                            .isNotNull()
                            .isEqualTo(expectedBusinessTitle);
                });

    }

    @Test
    void findBooksWithConflictTimesByScheduleId() {
        // Given
        Long scheduleId = schedule.getId();
        LocalDateTime startTime = LocalDateTime.now().minusHours(2);
        LocalDateTime endTime = LocalDateTime.now().minusHours(1);

        // When
        long conflictingBooks = bookRepository.findBooksWithConflictTimesByScheduleId(scheduleId, startTime, endTime);

        // Then
        Assertions.assertThat(conflictingBooks)
                .isPositive()
                .isEqualTo(1L);
    }

    @Test
    void findBooksWithConflictTimesByScheduleId_NotFound() {
        // Given
        Long scheduleId = schedule.getId();
        LocalDateTime startTime = LocalDateTime.now().plusDays(2);
        LocalDateTime endTime = LocalDateTime.now().plusDays(1);

        // When
        long conflictingBooks = bookRepository.findBooksWithConflictTimesByScheduleId(scheduleId, startTime, endTime);

        // Then
        Assertions.assertThat(conflictingBooks)
                .isZero();
    }

    @Test
    void checkForBreakConflicts() {
        // Create and save ScheduleSettings
        ScheduleSettings scheduleSettings = new ScheduleSettings();
        scheduleSettings.setSchedule(schedule);
        scheduleSettings.setBreakBetweenBookings(10);

        // Create and save BreakPeriods
        BreakPeriod break1 = new BreakPeriod();
        break1.setStart(LocalTime.of(12, 0));
        break1.setEnd(LocalTime.of(13, 0));
        break1.setSettings(scheduleSettings);
        breakPeriodRepository.save(break1);

        BreakPeriod break2 = new BreakPeriod();
        break2.setStart(LocalTime.of(14, 0));
        break2.setEnd(LocalTime.of(14, 30));
        break2.setSettings(scheduleSettings);
        breakPeriodRepository.save(break2);

        Long scheduleId = schedule.getId();

        // When
        boolean hasConflict = bookRepository.checkForBreakConflicts(scheduleId, LocalTime.of(14, 10), LocalTime.of(15, 0));
        boolean hasNoConflict = bookRepository.checkForBreakConflicts(scheduleId, LocalTime.of(17, 30), LocalTime.of(18, 0));

        // Then
        Assertions.assertThat(hasConflict)
                .isTrue();

        Assertions.assertThat(hasNoConflict)
                .isFalse();
    }

    @Test
    void checkForBreakConflicts_NoConflict() {
        // Given
        Long scheduleId = schedule.getId();
        LocalTime startTime = LocalTime.now().plusHours(15);
        LocalTime endTime = LocalTime.now().plusHours(20);

        // When
        boolean hasConflict = bookRepository.checkForBreakConflicts(scheduleId, startTime, endTime);

        // Then
        Assertions.assertThat(hasConflict)
                .isFalse();
    }

    @Test
    void findUserHistoryById() {
        // Given
        Long userId = user.getId();

        // When
        List<BookInfoDto> userHistory = bookRepository.findUserHistoryById(userId);

        // Then
        Assertions.assertThat(userHistory)
                .isNotNull()
                .isNotEmpty()
                .hasSize(5)
                .allMatch(bookInfoDto ->
                        bookInfoDto.getOutputUserDto().getEmail().equals(user.getEmail()) &&
                                bookInfoDto.getFinishedAt().isBefore(LocalDateTime.now()));
    }
}