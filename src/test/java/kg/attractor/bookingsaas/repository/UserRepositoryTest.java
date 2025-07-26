package kg.attractor.bookingsaas.repository;

import kg.attractor.bookingsaas.dto.user.UserBookQuantityDto;
import kg.attractor.bookingsaas.enums.RoleEnum;
import kg.attractor.bookingsaas.models.*;
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
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private DayOfWeekRepository dayOfWeekRepository;

    private Service firstService;

    @BeforeEach
    void initialize() {
        // Create and save Roles
        Role role = new Role();
        role.setRoleName(RoleEnum.CLIENT);
        Role createdRole = roleRepository.save(role);

        // Create and save Users
        List<User> users = IntStream.range(0, 5)
                .mapToObj(i -> {
                    User user = new User();
                    user.setFirstName("User" + i);
                    user.setLastName("Last" + i);
                    user.setMiddleName("Middle" + i);
                    user.setEmail("user_test" + i + "@gmail.com");
                    user.setPhone("000000000" + i);
                    user.setPassword("password");
                    user.setRole(createdRole);
                    user.setBirthday(LocalDate.now().minusYears(20 + i));
                    user.setLogo("logo" + i + ".png");
                    return userRepository.save(user);
                })
                .toList();

        // Create and save City
        City city = new City();
        city.setName("Test City");
        City createdCity = cityRepository.save(city);

        //Create business category
        BusinessCategory businessCategory = new BusinessCategory();
        businessCategory.setName("Test Category");

        // Create Business
        Business business = new Business();
        business.setUser(users.get(0));
        business.setTitle("Business");
        business.setDescription("Description for business ");
        business.setBusinessAddress("Address ");
        business.setBusinessPhone("123-456-789");
        business.setBusinessEmail("business@gmail.com");
        business.setCity(createdCity);
        business.setBusinessCategory(businessCategory);
        businessRepository.save(business);

        // Create Services
        var services = IntStream.range(0, 5)
                .mapToObj(i -> {
                    Service newService = new Service();
                    newService.setServiceName("Test Service");
                    newService.setBusiness(business);
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
        this.firstService = services.get(0);
        Schedule schedule = new Schedule();
        schedule.setService(firstService);
        schedule.setStartTime(LocalTime.of(9, 0));
        schedule.setEndTime(LocalTime.of(17, 0));
        schedule.setIsAvailable(true);
        schedule.setMaxBookingSize(1);
        schedule.setDayOfWeek(dayOfWeek);
        var createdSchedule = scheduleRepository.save(schedule);

        // Create Book
        IntStream.range(0, 5).forEach(i -> {
            Book book = new Book();
            book.setUser(users.get(i));
            book.setSchedule(createdSchedule);
            book.setStartedAt(LocalDate.now().atStartOfDay().plusHours(i));
            book.setFinishedAt(LocalDate.now().atStartOfDay().plusHours(i + 1));
            bookRepository.save(book);
        });

        Book book = new Book();
        book.setUser(users.get(0));
        book.setSchedule(createdSchedule);
        book.setStartedAt(LocalDate.now().atStartOfDay().plusHours(6));
        book.setFinishedAt(LocalDate.now().atStartOfDay().plusHours(6 + 1));
        bookRepository.save(book);
    }

    @Test
    void findByEmail() {
        // Given
        String email = "user_test2@gmail.com";

        // When
        Optional<User> user = userRepository.findByEmail(email);

        // Then
        Assertions.assertThat(user)
                .isPresent()
                .get()
                .extracting(User::getEmail)
                .isEqualTo(email);
    }

    @Test
    void existsByEmail() {
        // Given
        String email = "user_test2@gmail.com";

        // When
        boolean exists = userRepository.existsByEmail(email);

        // Then
        Assertions.assertThat(exists)
                .isTrue();
    }

    @Test
    void existByPhoneNumber() {
        // Given
        String phone = "0000000001";

        // When
        boolean exists = userRepository.existByPhoneNumber(phone);

        // Then
        Assertions.assertThat(exists)
                .isTrue();
    }

    @Test
    void findUserByPhone() {
        // Given
        String phone = "0000000002";

        // When
        Optional<User> user = userRepository.findUserByPhone(phone);

        // Then
        Assertions.assertThat(user)
                .isPresent()
                .get()
                .extracting(User::getPhone)
                .isEqualTo(phone);
    }

    @Test
    void findUsersByBusinessId() {
        // Given
        Long businessId = 1L;

        // When
        var users = userRepository.findUsersByBusinessId(businessId);

        // Then
        Assertions.assertThat(users)
                .isNotNull()
                .isNotEmpty()
                .hasSize(5)
                .extracting(User::getEmail)
                .containsExactlyInAnyOrder(
                        "user_test0@gmail.com",
                        "user_test1@gmail.com",
                        "user_test2@gmail.com",
                        "user_test3@gmail.com",
                        "user_test4@gmail.com"
                );
    }

    @Test
    void findUserByBusinessTitle() {
        // Given
        String businessTitle = "Business";

        // When
        Pageable pageable = PageRequest.of(0, 10, Sort.by("firstName"));
        Page<User> users = userRepository.findUserByBusinessTitle(businessTitle, pageable);

        // Then
        Assertions.assertThat(users)
                .isNotNull()
                .isNotEmpty()
                .hasSize(5)
                .extracting(User::getEmail)
                .containsExactlyInAnyOrder(
                        "user_test0@gmail.com",
                        "user_test1@gmail.com",
                        "user_test2@gmail.com",
                        "user_test3@gmail.com",
                        "user_test4@gmail.com"
                );
    }

    @Test
    void findUsersByServiceId() {
        // Given
        Long serviceId = firstService.getId();

        // When
        Pageable pageable = PageRequest.of(0, 10, Sort.by("firstName"));
        Page<User> users = userRepository.findUsersByServiceId(serviceId, pageable);

        // Then
        Assertions.assertThat(users)
                .isNotNull()
                .isNotEmpty()
                .hasSize(5)
                .extracting(User::getEmail)
                .containsExactlyInAnyOrder(
                        "user_test0@gmail.com",
                        "user_test1@gmail.com",
                        "user_test2@gmail.com",
                        "user_test3@gmail.com",
                        "user_test4@gmail.com"
                );
    }

    @Test
    void findUsersWithBooksQuantityAtLeastTwo() {
        // When
        Pageable pageable = PageRequest.of(0, 10, Sort.by("firstName"));
        Page<UserBookQuantityDto> usersWithBooks = userRepository.findUsersWithBooksQuantityAtLeastTwo(pageable);

        // Then
        Assertions.assertThat(usersWithBooks)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .extracting(UserBookQuantityDto::getEmail)
                .contains("user_test0@gmail.com");
    }
}