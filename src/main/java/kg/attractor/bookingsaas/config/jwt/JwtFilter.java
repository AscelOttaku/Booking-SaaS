package kg.attractor.bookingsaas.config.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kg.attractor.bookingsaas.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String tokenHeader = request.getHeader("Authorization");
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            String token = tokenHeader.substring(7);
            if (StringUtils.hasText(token)) {
                try {
                    String email;
                    try {
                        email = jwtService.validateToken(token);
                    } catch (MalformedJwtException e) {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
                        return;
                    } catch (ExpiredJwtException e) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
                        return;
                    }
                    String finalEmail = email;
                    UserDetails userDetails = userDetailsService.loadUserByUsername(finalEmail);
                    SecurityContextHolder.getContext().setAuthentication(
                            new UsernamePasswordAuthenticationToken(
                                    userDetails.getUsername(),
                                    null,
                                    userDetails.getAuthorities()
                            ));
                } catch (JWTVerificationException e) {
                    response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST,
                            "Неправильный токен!");
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}