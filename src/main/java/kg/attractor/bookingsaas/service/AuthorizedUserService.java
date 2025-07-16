package kg.attractor.bookingsaas.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface AuthorizedUserService {
    UserDetails getAuthUser();

    Long getAuthorizedUserId();
}
