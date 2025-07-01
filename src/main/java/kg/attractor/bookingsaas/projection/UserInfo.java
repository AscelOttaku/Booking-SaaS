package kg.attractor.bookingsaas.projection;

import java.time.LocalDate;
import java.util.List;

public interface UserInfo {
    Long getId();
    String getFirstName();
    String getLastName();
    String getMiddleName();
    String getPassword();
    String getPhone();
    LocalDate getBirthday();
    String getEmail();
    String getLogo();
    UserBusinessServiceProjection.RoleInfo getRole();
    List<BookInfo> getBooks();
}
