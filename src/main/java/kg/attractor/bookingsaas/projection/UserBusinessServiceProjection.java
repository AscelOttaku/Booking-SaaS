package kg.attractor.bookingsaas.projection;

import java.time.LocalTime;

public interface UserBusinessServiceProjection {
    UserInfo getUser();
    BusinessInfo getBusiness();
    ServiceInfo getServices();

    interface RoleInfo {
        String getRoleName();
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
}
