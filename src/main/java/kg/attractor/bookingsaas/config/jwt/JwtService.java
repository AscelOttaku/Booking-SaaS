package kg.attractor.bookingsaas.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import kg.attractor.bookingsaas.models.User;
import kg.attractor.bookingsaas.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final UserRepository userAccountRepository;

    @Value("${spring.jwt.secret_key}")
    private String SECRET_KEY;

    public String generateToken(String email) {
        return JWT.create()
                .withClaim("email", email)
                .withIssuedAt(new Date())
                .withExpiresAt(Date.from(ZonedDateTime.now().plusWeeks(3).toInstant()))
                .sign(Algorithm.HMAC512(SECRET_KEY));
    }

    public String validateToken(String token) {
        JWTVerifier jwtVerifier = JWT
                .require(Algorithm.HMAC512(SECRET_KEY))
                .build();
        DecodedJWT jwt = jwtVerifier.verify(token);
        return jwt.getClaim("email").asString();
    }

    public Optional<User> getAuthenticationUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userAccountRepository.findByEmail(email);
    }
}