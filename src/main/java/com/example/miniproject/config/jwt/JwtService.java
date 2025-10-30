package com.example.miniproject.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.miniproject.entity.User;
import com.example.miniproject.exception.NotFoundException;
import com.example.miniproject.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
@RequiredArgsConstructor
public class JwtService {
    @Value("${spring.security.jwt.secret}")
    private String secret;
    private final UserRepository userRepo;

    public String generateToken(User user) {
        ZonedDateTime now = ZonedDateTime.now();
        return JWT.create()
                .withClaim("id", user.getId())
                .withClaim("email", user.getEmail())
                .withClaim("role", user.getRole().name())
                .withIssuedAt(now.toInstant())
                .withExpiresAt(now.plusHours(24).toInstant()) // 24 саат
                .sign(getAlgorithm());
    }

    public User verifyToken(String token) {
        try {
            Algorithm algorithm = getAlgorithm();
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);

            String email = jwt.getClaim("email").asString();

            return userRepo.findByEmail(email)
                    .orElseThrow(() -> new NotFoundException("Email not found: " + email));
        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT token", e);
        }
    }
    public String extractEmail(String token) {
        return com.auth0.jwt.JWT.decode(token).getClaim("email").asString();
        // или getSubject(), если вы кладёте email в subject при генерации
        // return com.auth0.jwt.JWT.decode(token).getSubject();
    }

    public String extractRole(String token) {
        String role = com.auth0.jwt.JWT.decode(token).getClaim("role").asString();
        if (role == null || role.isBlank()) role = "USER";
        return role;
    }

    public void verifyTokenOrThrow(String token) {
        var algorithm = com.auth0.jwt.algorithms.Algorithm.HMAC256(secret);
        com.auth0.jwt.JWT.require(algorithm).build().verify(token); // бросит исключение, если невалиден/просрочен
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(secret);
    }
}