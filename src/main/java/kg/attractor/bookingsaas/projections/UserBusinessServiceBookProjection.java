package kg.attractor.bookingsaas.projections;

import java.time.LocalDateTime;
import java.util.List;

public interface UserBusinessServiceBookProjection {
    BusinessView getBusiness();
    UserView getUser();
    List<ServiceView> getServices();

    interface BusinessView {
        Long getId();
        String getTitle();
    }

    interface UserView {
        Long getId();
        String getUsername();
    }

    interface ServiceView {
        Long getId();
        String getName();
    }

    interface BookView {
        Long getId();
        LocalDateTime getBookingTime();
    }
}
