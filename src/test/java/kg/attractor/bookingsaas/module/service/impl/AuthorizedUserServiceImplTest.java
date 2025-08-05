package kg.attractor.bookingsaas.module.service.impl;

import kg.attractor.bookingsaas.enums.RoleEnum;
import kg.attractor.bookingsaas.models.User;
import kg.attractor.bookingsaas.service.impl.AuthorizedUserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AuthorizedUserServiceImplTest {
    private final AuthorizedUserServiceImpl authorizedUserService = new AuthorizedUserServiceImpl();

    private User user;

    @BeforeEach
    void setUser() {
        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("encodedPassword");
        user.setPhone("1234567890");

        List<GrantedAuthority> grantedAuthorities = List.of(new SimpleGrantedAuthority(RoleEnum.CLIENT.name()));
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, grantedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void getAuthUser() {
        // When
        UserDetails userDetails = authorizedUserService.getAuthUser();

        // Then
        assertThat(userDetails)
                .isNotNull()
                .extracting(UserDetails::getUsername)
                .isEqualTo("john.doe@example.com");
    }

    @Test
    void getAuthorizedUserId() {
        // When
        Long userId = authorizedUserService.getAuthorizedUserId();

        // Then
        assertThat(userId).isEqualTo(user.getId());
    }
}