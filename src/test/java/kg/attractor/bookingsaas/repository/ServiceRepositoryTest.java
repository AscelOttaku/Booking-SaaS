package kg.attractor.bookingsaas.repository;

import kg.attractor.bookingsaas.enums.BookStatus;
import kg.attractor.bookingsaas.enums.RoleEnum;
import kg.attractor.bookingsaas.models.*;
import kg.attractor.bookingsaas.projection.UserBookServiceProjection;
import kg.attractor.bookingsaas.projection.UserInfo;
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
import java.util.Optional;
import java.util.stream.IntStream;

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
                    newBusiness.setTitle("Test Business");
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
        serviceRepository.saveAll(services);

        // Create and save DayOfWeek
        DayOfWeek dayOfWeek = new DayOfWeek();
        dayOfWeek.setName("Monday");
        dayOfWeek.setIsWorking(true);
        dayOfWeek = dayOfWeekRepository.save(dayOfWeek);

        // Create and save Schedule
        Schedule schedule = new Schedule();
        schedule.setService(services.get(0));
        schedule.setStartTime(LocalTime.of(9, 0));
        schedule.setEndTime(LocalTime.of(17, 0));
        schedule.setIsAvailable(true);
        schedule.setMaxBookingSize(1);
        schedule.setDayOfWeek(dayOfWeek);
        scheduleRepository.save(schedule);

        // Create and save Book
        IntStream.range(0, 5)
                .forEach(i -> {
                    Book book = new Book();
                    book.setUser(users.get(i));
                    book.setSchedule(schedule);
                    book.setStartedAt(LocalDateTime.now());
                    book.setFinishedAt(LocalDateTime.now().plusHours(1));
                    book.setStatus(BookStatus.ACCEPTED);
                    bookRepository.save(book);
                });
    }

    @Test
    void findAllByBusinessTitle() {

        // When
        Pageable pageable = PageRequest.of(0, 10, Sort.by("serviceName"));
        Page<Service> services = serviceRepository.findAllByBusinessTitle("Test Business", pageable);

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

        // When
        Optional<Integer> foundServiceDuration = serviceRepository.findServiceDurationByScheduleId(1L);

        // Then
        Assertions.assertThat(foundServiceDuration)
                .isPresent()
                .get()
                .isEqualTo(60);
    }

    @Test
    void findClientsByServiceId() {

        // When
        Pageable pageable = PageRequest.of(0, 10);
        Page<UserBookServiceProjection> userBookServiceProjections = serviceRepository.findClientsByServiceId(1L, pageable);

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
    void findMostPopularServiceByBusinessTitle() {

        // When

        // Then

    }

    @Test
    void findServicesByBusinessTitle() {

        // When

        // Then

    }
}