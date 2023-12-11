package app.security;

import io.jsonwebtoken.Claims;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface JwtProvider {
    Optional<Claims> extractClaims(String token);

    Optional<List<String>> extractRoles(@NonNull String token);
}
