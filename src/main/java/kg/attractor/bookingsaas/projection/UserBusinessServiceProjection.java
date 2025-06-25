package kg.attractor.bookingsaas.projection;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface UserBusinessServiceProjection {
    UserInfo getUser();
    BusinessInfo getBusiness();
    ServiceInfo getServices();

    interface UserInfo {
        Long getId();
        String getFirstName();
        String getLastName();
        String getMiddleName();
        String getPassword();
        String getPhone();
        LocalDate getBirthday();
        String getEmail();
        String getLogo();
        RoleInfo getRole();
    }

    interface BusinessInfo {
        Long getId();
        UserInfo getUser();
        String getTitle();
        String getDescription();
        LocalDateTime getCreatedAt();
        LocalDateTime getUpdatedAt();
        List<ServiceInfo> getServices();
        ServiceInfo getService();
    }

    interface RoleInfo {
        String getRoleName();
    }

    interface ServiceInfo {
        Long getId();
        BusinessInfo getBusiness();
        String getServiceName();
        Integer getDuration();
        BigDecimal getPrice();
    }

    interface ScheduleInfo {
        Long getId();
        DayOfWeekInfo getDayOfWeek();
        LocalTime getStartTime();
        LocalTime getEndTime();
        Boolean getIsAvailable();
        Integer getMaxBookingSize();

        interface DayOfWeekInfo {
            Long getId();
            String getName();
        }
    }

    interface BookInfo {
        Long getId();
        ScheduleInfo getSchedule();
        LocalDateTime getStartedAt();
        LocalDateTime getFinishedAt();
    }
}
