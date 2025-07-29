package kg.attractor.bookingsaas.util;

import kg.attractor.bookingsaas.enums.BookStatus;
import kg.attractor.bookingsaas.models.*;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@UtilityClass
public class TestEntityFactory {

    public static User createUser(Long id, String email) {
        User user = new User();
        user.setId(id);
        user.setFirstName("First" + id);
        user.setLastName("Last" + id);
        user.setEmail(email);
        user.setPassword("password");
        user.setPhone("123456789" + id);
        return user;
    }

    public static Business createBusiness(Long id, User owner, String title) {
        Business business = new Business();
        business.setId(id);
        business.setUser(owner);
        business.setTitle(title);
        business.setDescription("Description for " + title);
        return business;
    }

    public static Service createService(Long id, Business business, String name) {
        Service service = new Service();
        service.setId(id);
        service.setBusiness(business);
        service.setServiceName(name);
        service.setDurationInMinutes(60);
        service.setPrice(BigDecimal.valueOf(100));
        return service;
    }

    public static Schedule createSchedule(Long id, Service service) {
        Schedule schedule = new Schedule();
        schedule.setId(id);
        schedule.setService(service);
        schedule.setStartTime(LocalTime.of(9, 0));
        schedule.setEndTime(LocalTime.of(10, 0));
        schedule.setIsAvailable(true);
        schedule.setMaxBookingSize(5);
        return schedule;
    }

    public static Book createBook(Long id, Schedule schedule, User user) {
        Book book = new Book();
        book.setId(id);
        book.setSchedule(schedule);
        book.setStartedAt(LocalDateTime.now());
        book.setFinishedAt(LocalDateTime.now().plusHours(1));
        book.setUser(user);
        book.setStatus(BookStatus.ACCEPTED);
        return book;
    }
}