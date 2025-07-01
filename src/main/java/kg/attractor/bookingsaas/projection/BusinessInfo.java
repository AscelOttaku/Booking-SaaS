package kg.attractor.bookingsaas.projection;

import java.time.LocalDateTime;
import java.util.List;

public interface BusinessInfo {
    Long getId();
    UserInfo getUser();
    String getTitle();
    String getDescription();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
    List<ServiceInfo> getServices();
    ServiceInfo getService();
}
