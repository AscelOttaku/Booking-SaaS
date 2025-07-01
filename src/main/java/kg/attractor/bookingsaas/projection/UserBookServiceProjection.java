package kg.attractor.bookingsaas.projection;

public interface UserBookServiceProjection {
    ServiceInfo getService();
    UserInfo getUser();
    BookInfo getBook();
}