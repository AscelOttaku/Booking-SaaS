package kg.attractor.bookingsaas.repository;

import kg.attractor.bookingsaas.enums.RoleEnum;
import kg.attractor.bookingsaas.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
            book.setStartedAt(LocalDateTime.now().plusHours(i));
            book.setFinishedAt(LocalDateTime.now().plusHours(i + 1));
            bookRepository.save(book);
        }
    }

    @Test
    void findAllBooksByServicesId() {
    }

    @Test
    void findAllBooksByBusinessTitle() {
    }

    @Test
    void findAllUsersBookedHistory() {
    }

    @Test
    void findBooksWithConflictTimesByScheduleId() {
    }

    @Test
    void checkForBreakConflicts() {
    }

    @Test
    void findUserHistoryById() {
    }

    @Test
    void findAllBooksByScheduleId() {
    }
}