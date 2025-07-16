package kg.attractor.bookingsaas.service.impl;

import kg.attractor.bookingsaas.models.User;
import kg.attractor.bookingsaas.service.AuthorizedUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizedUserServiceImpl implements AuthorizedUserService {

    @Override
    public UserDetails getAuthUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken)
            throw new IllegalArgumentException("user is not authenticated");

        return (UserDetails) authentication.getPrincipal();
    }

    @Override
    public Long getAuthorizedUserId() {
        User authUser = (User) getAuthUser();
        return authUser.getId();
    }
}
