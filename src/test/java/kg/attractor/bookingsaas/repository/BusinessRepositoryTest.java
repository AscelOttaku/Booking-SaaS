package kg.attractor.bookingsaas.repository;

import kg.attractor.bookingsaas.enums.BookStatus;
import kg.attractor.bookingsaas.enums.RoleEnum;
import kg.attractor.bookingsaas.models.*;
import kg.attractor.bookingsaas.projection.UserBusinessServiceProjection;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ActiveProfiles("test")
class BusinessRepositoryTest {

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private DayOfWeekRepository dayOfWeekRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CityRepository cityRepository;

    private Business business;
    private Service services;
    private User user;

    @BeforeEach
    void setUpBeforeClass() {
        // Create and save Role
        Role role = new Role();
        role.setRoleName(RoleEnum.CLIENT);
        role = roleRepository.save(role);

        // Create and save User
        User newUser = new User();
        newUser.setFirstName("John");
        newUser.setLastName("Doe");
        newUser.setMiddleName("Middle");
        newUser.setEmail("john.doe@example.com");
        newUser.setPhone("1234567890");
        newUser.setPassword("password");
        newUser.setRole(role);
        newUser.setBirthday(LocalDate.now().minusYears(25));
        newUser.setLogo("logo.png");
        this.user = userRepository.save(newUser);

        // Create and save City
        City city = new City();
        city.setName("Test City");
        city = cityRepository.save(city);

        //Create business category
        BusinessCategory businessCategory = new BusinessCategory();
        businessCategory.setName("Test Category");

        // Create and save Business
        Business newBusiness = new Business();
        newBusiness.setUser(user);
        newBusiness.setTitle("Test Business");
        newBusiness.setDescription("This is a test business.");
        newBusiness.setBusinessAddress("123 Test St");
        newBusiness.setBusinessPhone("123-456-7890");
        newBusiness.setBusinessEmail("test@gmail.com");
        newBusiness.setCity(city);
        newBusiness.setBusinessCategory(businessCategory);
        this.business = businessRepository.save(newBusiness);

        // Create and save Service
        Service newService = new Service();
        newService.setServiceName("Test Service");
        newService.setBusiness(business);
        newService.setDurationInMinutes(60);
        newService.setPrice(BigDecimal.valueOf(100.00));
        this.services = serviceRepository.save(newService);

        // Create and save DayOfWeek
        DayOfWeek dayOfWeek = new DayOfWeek();
        dayOfWeek.setName("Monday");
        dayOfWeek.setIsWorking(true);
        dayOfWeek = dayOfWeekRepository.save(dayOfWeek);

        // Create and save Schedule
        Schedule schedule = new Schedule();
        schedule.setService(newService);
        schedule.setStartTime(LocalTime.of(9, 0));
        schedule.setEndTime(LocalTime.of(17, 0));
        schedule.setIsAvailable(true);
        schedule.setMaxBookingSize(1);
        schedule.setDayOfWeek(dayOfWeek);
        schedule = scheduleRepository.save(schedule);

        // Create and save Book
        Book book = new Book();
        book.setUser(newUser);
        book.setSchedule(schedule);
        book.setStartedAt(LocalDateTime.now());
        book.setFinishedAt(LocalDateTime.now().plusHours(1));
        book.setStatus(BookStatus.ACCEPTED);
        bookRepository.save(book);
    }

    @Test
    void getUserBusinessServiceBookByBusinessId() {
        // When
        List<UserBusinessServiceProjection> result = businessRepository.getUserBusinessServiceBookByBusinessId(business.getId());

        // Then
        Assertions.assertThat(result).isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(result.get(0)).isNotNull();

        UserBusinessServiceProjection userBusinessServiceProjection = result.get(0);
        Assertions.assertThat(userBusinessServiceProjection.getUser()).isNotNull();
        Assertions.assertThat(userBusinessServiceProjection.getUser().getId())
                .isEqualTo(user.getId());

        Assertions.assertThat(userBusinessServiceProjection.getBusiness()).isNotNull();
        Assertions.assertThat(userBusinessServiceProjection.getBusiness().getId())
                .isEqualTo(business.getId());

        Assertions.assertThat(userBusinessServiceProjection.getServices()).isNotNull();
        Assertions.assertThat(userBusinessServiceProjection.getServices().getId())
                .isEqualTo(services.getId());
    }

    @Test
    void findByNameContaining() {
        // Given
        String searchTerm = "Test Business";

        //When
        List<Business> businesses = businessRepository.findByNameContaining(searchTerm);

        // Then
        Assertions.assertThat(businesses)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .extracting(Business::getTitle)
                .contains(searchTerm);

        Business firstBusiness = businesses.get(0);
        Assertions.assertThat(firstBusiness.getId()).isEqualTo(business.getId());
        Assertions.assertThat(firstBusiness.getUser().getId()).isEqualTo(user.getId());
        Assertions.assertThat(firstBusiness.getServices())
                .isNotNull()
                .isNotEmpty()
                .extracting(Service::getId)
                .contains(services.getId());
    }

    @Test
    void existsByTitle() {
        // Given
        String title = "Test Business";

        // When
        boolean exists = businessRepository.existsByTitle(title);

        // Then
        Assertions.assertThat(exists).isTrue();

        // Check for a non-existing title
        String nonExistingTitle = "Non-Existing Business";
        boolean nonExists = businessRepository.existsByTitle(nonExistingTitle);
        Assertions.assertThat(nonExists).isFalse();
    }

    @Test
    void countBusinessesByUserId() {
        // Given
        Long userId = user.getId();

        // When
        long count = businessRepository.countBusinessesByUserId(userId);

        // Then
        Assertions.assertThat(count).isEqualTo(1L);
    }

    @Test
    void findByTitle() {
        // Given
        String businessTitle = "Test Business";

        // When
        var optionalBusiness = businessRepository.findByTitle(businessTitle);

        // Then
        Assertions.assertThat(optionalBusiness).isPresent();
        Assertions.assertThat(optionalBusiness.get().getId()).isEqualTo(business.getId());
        Assertions.assertThat(optionalBusiness.get().getUser().getId()).isEqualTo(user.getId());
        Assertions.assertThat(optionalBusiness.get().getServices())
                .isNotNull()
                .isNotEmpty()
                .extracting(Service::getId)
                .contains(services.getId());
    }

    @Test
    void findBusiness() {
        // Given
        int pageNumber = 0;
        int pageSize = 10;

        // When
        var pageable = PageRequest.of(pageNumber, pageSize);
        var businessPage = businessRepository.findBusiness(pageable);

        // Then
        Assertions.assertThat(businessPage).isNotNull();
        Assertions.assertThat(businessPage.getContent()).isNotNull();
        Assertions.assertThat(businessPage.getContent()).isNotEmpty();
        Assertions.assertThat(businessPage.getTotalElements()).isGreaterThan(0);

        // Check if the business is present in the page
        Assertions.assertThat(businessPage.getContent())
                .extracting(Business::getId)
                .contains(business.getId());
    }
}