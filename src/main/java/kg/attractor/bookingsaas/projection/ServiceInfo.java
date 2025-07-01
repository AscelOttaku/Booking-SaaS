package kg.attractor.bookingsaas.projection;

import java.math.BigDecimal;

public interface ServiceInfo {
    Long getId();
    BusinessInfo getBusiness();
    String getServiceName();
    Integer getDurationInMinutes();
    BigDecimal getPrice();
}
