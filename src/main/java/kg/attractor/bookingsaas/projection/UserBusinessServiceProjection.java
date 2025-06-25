package kg.attractor.bookingsaas.projection;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
        List<BookInfo> getBooks();
    }

    interface BookInfo {
        Long getId();
        ServiceInfo getServices();
        LocalDateTime getStartedAt();
        LocalDateTime getFinishedAt();
    }
}
