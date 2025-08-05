package kg.attractor.bookingsaas.module.repository;

import kg.attractor.bookingsaas.enums.BookStatus;
import kg.attractor.bookingsaas.enums.RoleEnum;
import kg.attractor.bookingsaas.models.*;
import kg.attractor.bookingsaas.projection.UserBookServiceProjection;
import kg.attractor.bookingsaas.projection.UserInfo;
import kg.attractor.bookingsaas.repository.*;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ActiveProfiles("test")
class ServiceRepositoryTest {

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private DayOfWeekRepository dayOfWeekRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private BookRepository bookRepository;

    private Schedule schedule;
    private Service firstService;

    @BeforeEach
    void setUp() {
        // Create and save Role
        Role role = new Role();
        role.setRoleName(RoleEnum.CLIENT);
        Role createdRole = roleRepository.save(role);

        // Create and save User
        var users = IntStream.range(0, 5)
                .mapToObj(i -> {
                    User newUser = new User();
                    newUser.setFirstName("John");
                    newUser.setLastName("Doe");
                    newUser.setMiddleName("Middle");
                    newUser.setEmail("john" + i + "@gmail.com");
                    newUser.setPhone("1234567890");
                    newUser.setPassword("password");
                    newUser.setRole(createdRole);
                    newUser.setBirthday(LocalDate.now().minusYears(25));
                    newUser.setLogo("logo.png");
                    return userRepository.save(newUser);
                })
                .toList();

        // Create and save City
        City city = new City();
        city.setName("Test City");
        City createdCity = cityRepository.save(city);

        //Create business category
        BusinessCategory businessCategory = new BusinessCategory();
        businessCategory.setName("Test Category");

        // Create and save Business
        var businesses = IntStream.range(0, 5)
                .mapToObj(i -> {
                    Business newBusiness = new Business();
                    newBusiness.setUser(users.get(0));
                    newBusiness.setTitle("Test Business " + i);
                    newBusiness.setDescription("This is a test business.");
                    newBusiness.setBusinessAddress("123 Test St");
                    newBusiness.setBusinessPhone("123-456-7890");
                    newBusiness.setBusinessEmail("test@gmail.com");
                    newBusiness.setCity(createdCity);
                    newBusiness.setBusinessCategory(businessCategory);
                    return newBusiness;
                })
                .toList();
        businessRepository.saveAll(businesses);

        // Create and save Service
        var services = IntStream.range(0, 5)
                .mapToObj(i -> {
                    Service newService = new Service();
                    newService.setServiceName("Test Service");
                    newService.setBusiness(businesses.get(0));
                    newService.setDurationInMinutes(60 * (i + 1));
                    newService.setPrice(BigDecimal.valueOf(100.00));
                    return newService;
                })
                .toList();
        services = serviceRepository.saveAll(services);
        firstService = services.get(0);

        // Create and save DayOfWeek
        DayOfWeek dayOfWeek = new DayOfWeek();
        dayOfWeek.setName("Monday");
        dayOfWeek.setIsWorking(true);
        dayOfWeek = dayOfWeekRepository.save(dayOfWeek);

        // Create and save Schedule
        Service service = services.get(0);
        log.info("Service Name: {}, Service duration: {}", service.getServiceName(), service.getDurationInMinutes());
        schedule = new Schedule();
        schedule.setService(service);
        schedule.setStartTime(LocalTime.of(9, 0));
        schedule.setEndTime(LocalTime.of(17, 0));
        schedule.setIsAvailable(true);
        schedule.setMaxBookingSize(1);
        schedule.setDayOfWeek(dayOfWeek);
        scheduleRepository.save(schedule);

        // Associate Schedule with Service
        services.get(0).setSchedules(List.of(schedule));

        // Create and save Book
        var createdBooks = IntStream.range(0, 5)
                .mapToObj(i -> {
                    Book book = new Book();
                    book.setUser(users.get(i));
                    book.setSchedule(schedule);
                    book.setStartedAt(LocalDateTime.now());
                    book.setFinishedAt(LocalDateTime.now().plusHours(1));
                    book.setStatus(BookStatus.ACCEPTED);
                    return bookRepository.save(book);
                })
                .toList();

        // Associate Books with Schedule
        schedule.setBooks(createdBooks);
    }

    @Test
    void findAllByBusinessTitle() {

        // When
        Pageable pageable = PageRequest.of(0, 10, Sort.by("serviceName"));
        Page<Service> services = serviceRepository.findAllByBusinessTitle("Test Business 0", pageable);

        // Then
        Assertions.assertThat(services)
                .isNotNull()
                .isNotEmpty()
                .hasSize(5)
                .extracting(Service::getServiceName)
                .allMatch(name -> name.equals("Test Service"));
    }

    @Test
    void findServiceDurationByScheduleId() {
        // Given
        Long scheduleId = schedule.getId();

        // When
        Optional<Integer> foundServiceDuration = serviceRepository.findServiceDurationByScheduleId(scheduleId);

        // Then
        Assertions.assertThat(foundServiceDuration)
                .isPresent()
                .get()
                .isEqualTo(60);
    }

    @Test
    void findClientsByServiceId() {
        // Given
        Long serviceId = firstService.getId();

        // When
        Pageable pageable = PageRequest.of(0, 10);
        Page<UserBookServiceProjection> userBookServiceProjections = serviceRepository.findClientsByServiceId(serviceId, pageable);

        // Then
        Assertions.assertThat(userBookServiceProjections)
                .isNotNull()
                .isNotEmpty()
                .hasSize(5)
                .extracting(UserBookServiceProjection::getUser)
                .isNotNull()
                .extracting(UserInfo::getEmail)
                .containsExactlyInAnyOrder("john0@gmail.com", "john1@gmail.com", "john2@gmail.com", "john3@gmail.com", "john4@gmail.com");
    }

    @Test
    @Rollback(value = false)
    void findMostPopularServiceByBusinessTitle() {
        // Given
        String businessTitle = "Test Business 0";

        // When
        Optional<Service> mostPopularService = serviceRepository.findMostPopularServiceByBusinessTitle(businessTitle);

        // Then
        mostPopularService.ifPresentOrElse(service -> {
            Assertions.assertThat(service)
                    .extracting(Service::getServiceName)
                    .isEqualTo("Test Service");

            Assertions.assertThat(service.getSchedules())
                    .as("Schedules must not be null or empty")
                    .isNotNull()
                    .isNotEmpty();

            List<Book> allBooks = service.getSchedules().stream()
                    .flatMap(schedule -> schedule.getBooks().stream())
                    .toList();

            Assertions.assertThat(allBooks)
                    .isNotNull()
                    .isNotEmpty()
                    .hasSize(5);

        }, () -> Assertions.fail("Most popular service not found for business title: " + businessTitle));
    }

    @Test
    void findServicesByBusinessTitle() {
        // Given
        String businessTitle = "Test Business 0";

        // When
        List<Service> services = serviceRepository.findServicesByBusinessTitle(businessTitle);

        // Then
        Assertions.assertThat(services)
                .isNotNull()
                .isNotEmpty()
                .hasSize(5)
                .extracting(Service::getServiceName)
                .allMatch(name -> name.equals("Test Service"));
    }
}