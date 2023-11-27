package app.airlinegateway.security;

import app.airlinegateway.exceptions.ErrorExtractRolesFromTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtProviderLite {

    private final SecretKey secret;

    public JwtProviderLite(
            @Value("${jwt.secret.access}")
            String jwtAccessSecret) {
        this.secret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret));
    }


    private Optional<Claims> extractClaims(String token) {
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
     *
     * @param token
     * @return Роли аутентифицированного пользователя + "ALL_ROLES"
     * для авторизации на endpoint открытых для аутентифицированных польхователях
     */
    public Set<String> extractRoles(@NonNull String token) {
        try {
            Set<String> roles = (Set<String>) extractClaims(token).get().get("roles", Set.class)
                   .stream()
                   .map(x -> {
                       return ((Map<String, String>)x).get("name");
                   }).collect(Collectors.toSet());
            roles.add("ALL_ROLES");
            return roles;
        } catch (Exception e) {
            log.error("Can't extract roles from token: {}", token);
            throw new ErrorExtractRolesFromTokenException();
        }
    }

}
