package kg.attractor.bookingsaas.repository;

import kg.attractor.bookingsaas.enums.RoleEnum;
import kg.attractor.bookingsaas.models.*;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
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
class BreakPeriodRepositoryTest {

    @Autowired
    private BreakPeriodRepository breakPeriodRepository;

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

    private Schedule schedule;
    private ScheduleSettings scheduleSettings;
    private BreakPeriod breakPeriod;

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
        Service service = new Service();
        service.setServiceName("Test Service");
        service.setBusiness(business);
        service.setDurationInMinutes(30);
        service.setPrice(BigDecimal.valueOf(100));
        service = serviceRepository.save(service);

        // DayOfWeek
        DayOfWeek dayOfWeek = new DayOfWeek();
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

        // ScheduleSettings
        scheduleSettings = new ScheduleSettings();
        scheduleSettings.setSchedule(schedule);
        scheduleSettings.setBreakBetweenBookings(10);

        // First BreakPeriod (12:00 - 13:00)
        breakPeriod = new BreakPeriod();
        breakPeriod.setStart(LocalTime.of(12, 0));
        breakPeriod.setEnd(LocalTime.of(13, 0));
        breakPeriod.setSettings(scheduleSettings);
        breakPeriodRepository.save(breakPeriod);

        // Second BreakPeriod (14:00 - 14:30)
        BreakPeriod breakPeriod2 = new BreakPeriod();
        breakPeriod2.setStart(LocalTime.of(14, 0));
        breakPeriod2.setEnd(LocalTime.of(14, 30));
        breakPeriod2.setSettings(scheduleSettings);
        breakPeriodRepository.save(breakPeriod2);

        // Third BreakPeriod (16:00 - 16:45)
        BreakPeriod breakPeriod3 = new BreakPeriod();
        breakPeriod3.setStart(LocalTime.of(16, 0));
        breakPeriod3.setEnd(LocalTime.of(16, 45));
        breakPeriod3.setSettings(scheduleSettings);
        breakPeriodRepository.save(breakPeriod3);
    }

    @Test
    void findByScheduleId() {
        // Given
        Long scheduleId = schedule.getId();

        // When
        var foundBreakPeriods = breakPeriodRepository.findByScheduleId(scheduleId);

        // Then
        Assertions.assertThat(foundBreakPeriods)
                .isNotNull()
                .isNotEmpty()
                .extracting(BreakPeriod::getStart, BreakPeriod::getEnd)
                .containsExactlyInAnyOrder(
                        Tuple.tuple(LocalTime.of(12, 0), LocalTime.of(13, 0)),
                        Tuple.tuple(LocalTime.of(14, 0), LocalTime.of(14, 30)),
                        Tuple.tuple(LocalTime.of(16, 0), LocalTime.of(16, 45))
                );
    }

    @Test
    void checkForBreakConflicts() {
        // Given
        LocalTime newStart = LocalTime.of(12, 30);
        LocalTime newEnd = LocalTime.of(13, 30);
        Long scheduleId = schedule.getId();

        // When
        boolean hasConflict = breakPeriodRepository.checkForBreakConflicts(scheduleId, newStart, newEnd);

        // Then
        Assertions.assertThat(hasConflict)
                .isTrue();
    }

    @Test
    void checkForBreakConflicts_NoConflict() {
        // Given
        LocalTime newStart = LocalTime.of(15, 0);
        LocalTime newEnd = LocalTime.of(15, 30);
        Long scheduleId = schedule.getId();

        // When
        boolean hasConflict = breakPeriodRepository.checkForBreakConflicts(scheduleId, newStart, newEnd);

        // Then
        Assertions.assertThat(hasConflict)
                .isFalse();
    }
}
