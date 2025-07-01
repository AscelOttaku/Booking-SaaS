package kg.attractor.bookingsaas.projection;

import java.time.LocalDateTime;

public interface BookInfo {
    Long getId();
    UserBusinessServiceProjection.ScheduleInfo getSchedule();
    LocalDateTime getStartedAt();
    LocalDateTime getFinishedAt();
    UserInfo getUser();
}
