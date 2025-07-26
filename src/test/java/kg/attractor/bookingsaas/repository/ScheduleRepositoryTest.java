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
import java.time.LocalTime;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ActiveProfiles("test")
class ScheduleRepositoryTest {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private DayOfWeekRepository dayOfWeekRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private Service service;
    private DayOfWeek dayOfWeek;
    private Schedule schedule;

    @BeforeEach
    void setUp() {
        // Role
        Role role = new Role();
        role.setRoleName(RoleEnum.CLIENT);
        role = roleRepository.save(role);

        // User
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setMiddleName("M");
        user.setEmail("john.doe@example.com");
        user.setPhone("1234567890");
        user.setPassword("password");
        user.setRole(role);
        user.setBirthday(LocalDate.of(1990, 1, 1));
        user = userRepository.save(user);

        // City
        City city = new City();
        city.setName("Test City");
        city = cityRepository.save(city);

        // BusinessCategory
        BusinessCategory category = new BusinessCategory();
        category.setName("Some Category");

        // Business
        Business business = new Business();
        business.setTitle("Test Business");
        business.setBusinessCategory(category);
        business.setCity(city);
        business.setUser(user);
        business = businessRepository.save(business);

        // Service
        service = new Service();
        service.setServiceName("Test Service");
        service.setBusiness(business);
        service.setDurationInMinutes(30);
        service.setPrice(BigDecimal.valueOf(100));
        service = serviceRepository.save(service);

        // DayOfWeek
        dayOfWeek = new DayOfWeek();
        dayOfWeek.setName("Monday");
        dayOfWeek.setIsWorking(true);
        dayOfWeek = dayOfWeekRepository.save(dayOfWeek);

        // Schedule
        schedule = new Schedule();
        schedule.setService(service);
        schedule.setDayOfWeek(dayOfWeek);
        schedule.setStartTime(LocalTime.of(9, 0));
        schedule.setEndTime(LocalTime.of(18, 0));
        schedule.setIsAvailable(true);
        schedule.setMaxBookingSize(10);
        schedule = scheduleRepository.save(schedule);
    }

    @Test
    void notExistByDayOfWeekIdAndServiceId() {
    }

    @Test
    void findDayOfWeekIdsByServiceIdAndDayOfWeekIds() {
    }

    @Test
    void findMaxBookingSizeByScheduleId() {
    }

    @Test
    void findScheduleTimeById() {
    }

    @Test
    void findAllByServiceId() {
    }

    @Test
    void findScheduleDurationById() {
    }
}