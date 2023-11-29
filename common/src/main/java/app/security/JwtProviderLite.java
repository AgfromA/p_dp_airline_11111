package app.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

import javax.crypto.SecretKey;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class JwtProviderLite implements JwtProvider {

    private final SecretKey secret;

    public JwtProviderLite(String jwtAccessSecret) {
        this.secret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret));
    }


    @Override
    public Optional<Claims> extractClaims(String token) {
        try {
            return Optional.of(Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token)
                    .getBody());
        } catch (ExpiredJwtException expEx) {
            log.error("Token expired", expEx);
        } catch (UnsupportedJwtException unsEx) {
            log.error("Unsupported jwt", unsEx);
        } catch (MalformedJwtException mjEx) {
            log.error("Malformed jwt", mjEx);
        } catch (SignatureException sEx) {
            log.error("Invalid signature", sEx);
        } catch (Exception e) {
            log.error("invalid token", e);
        }
        return Optional.empty();
    }

    /**
     * @param token без Bearer без токена
     * @return Роли аутентифицированного пользователя + "ALL_ROLES"
     * для авторизации на endpoint открытых для аутентифицированных польхователях
     */
    @Override
    public Optional<List<String>> extractRoles(@NonNull String token) {
        try {
            List<String> roles = (List<String>) extractClaims(token).get().get("roles", List.class)
                   .stream()
                   .map(x -> {
                       return ((Map<String, String>)x).get("name");
                   }).collect(Collectors.toList());
            roles.add("ALL_ROLES");
            return Optional.of(roles);
        } catch (Exception e) {
            log.error("Can't extract roles from token: {}", token);
            return Optional.empty();
        }
    }

}
