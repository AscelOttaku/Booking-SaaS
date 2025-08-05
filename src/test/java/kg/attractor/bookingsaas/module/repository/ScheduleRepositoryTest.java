package kg.attractor.bookingsaas.module.repository;

import kg.attractor.bookingsaas.dto.ScheduleTimeDto;
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
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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

    @Autowired
    private BreakPeriodRepository breakPeriodRepository;

    private Service service;
    private DayOfWeek monday;
    private DayOfWeek tuesday;
    private Schedule schedule;
    private ScheduleSettings scheduleSettings;

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

        // DayOfWeek Monday
        monday = new DayOfWeek();
        monday.setName("Monday");
        monday.setIsWorking(true);
        monday = dayOfWeekRepository.save(monday);

        // DayOfWeek Tuesday
        tuesday = new DayOfWeek();
        tuesday.setName("Tuesday");
        tuesday.setIsWorking(true);
        this.tuesday = dayOfWeekRepository.save(tuesday);

        // Schedule
        schedule = new Schedule();
        schedule.setService(service);
        schedule.setDayOfWeek(monday);
        schedule.setStartTime(LocalTime.of(9, 0));
        schedule.setEndTime(LocalTime.of(18, 0));
        schedule.setIsAvailable(true);
        schedule.setMaxBookingSize(10);
        schedule = scheduleRepository.save(schedule);

        // ScheduleSettings
        scheduleSettings = new ScheduleSettings();
        scheduleSettings.setSchedule(schedule);
        scheduleSettings.setBreakBetweenBookings(10);

        // BreakPeriodsBetweenBookings
        BreakPeriod breakPeriod = new BreakPeriod();
        breakPeriod.setStart(LocalTime.of(16, 0));
        breakPeriod.setEnd(LocalTime.of(16, 45));
        breakPeriod.setSettings(scheduleSettings);
        breakPeriodRepository.save(breakPeriod);
    }

    @Test
    void notExistByDayOfWeekIdAndServiceId() {
        // Given
        Long serviceId = service.getId();
        Long dayOfWeekId = tuesday.getId(); // Using Tuesday which is not scheduled for the service

        // When
        boolean isNotExist = scheduleRepository.notExistByDayOfWeekIdAndServiceId(dayOfWeekId, serviceId);

        // Then
        Assertions.assertThat(isNotExist)
                .isTrue();
    }

    @Test
    void notExistByDayOfWeekIdAndServiceId_False() {
        // Given
        Long serviceId = service.getId();
        Long dayOfWeekId = monday.getId(); // Using Monday which is scheduled for the service

        // When
        boolean isNotExist = scheduleRepository.notExistByDayOfWeekIdAndServiceId(dayOfWeekId, serviceId);

        // Then
        Assertions.assertThat(isNotExist)
                .isFalse();
    }

    @Test
    void findDayOfWeekIdsByServiceIdAndDayOfWeekIds() {
        // Given
        Long serviceId = service.getId();
        List<Long> dayOfWeekIds = List.of(monday.getId(), tuesday.getId());

        // When
        List<Long> foundDayOfWeekIds = scheduleRepository.findDayOfWeekIdsByServiceIdAndDayOfWeekIds(serviceId, dayOfWeekIds);

        // Then
        Assertions.assertThat(foundDayOfWeekIds)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .contains(schedule.getId());
    }

    @Test
    void findMaxBookingSizeByScheduleId() {
        // Given
        Long scheduleId = schedule.getId();

        // When
        int maxBookingSize = scheduleRepository.findMaxBookingSizeByScheduleId(scheduleId);

        // Then
        Assertions.assertThat(maxBookingSize)
                .isPositive()
                .isEqualTo(schedule.getMaxBookingSize());
    }

    @Test
    void findScheduleTimeById() {
        // Given
        Long scheduleId = schedule.getId();

        // When
        ScheduleTimeDto scheduleTimeDto = scheduleRepository.findScheduleTimeById(scheduleId);

        // Then
        Assertions.assertThat(scheduleTimeDto)
                .isNotNull()
                .extracting(ScheduleTimeDto::getStartTime, ScheduleTimeDto::getEndTime, ScheduleTimeDto::getScheduleId)
                .containsExactly(schedule.getStartTime(), schedule.getEndTime(), scheduleId);
    }

    @Test
    void findAllByServiceId() {
        // Given
        Long serviceId = service.getId();

        // When
        List<Schedule> schedules = scheduleRepository.findAllByServiceId(serviceId);

        // Then
        Assertions.assertThat(schedules)
                .isNotNull()
                .isNotEmpty()
                .allMatch(oneSchedule -> oneSchedule.getService().getId().equals(serviceId));
    }

    @Test
    void findScheduleDurationById() {
        // Given
        Long scheduleId = schedule.getId();

        // When
        int duration = scheduleRepository.findScheduleDurationById(scheduleId);

        // Then
        Assertions.assertThat(duration)
                .isPositive()
                .isEqualTo(scheduleSettings.getBreakBetweenBookings());
    }
}