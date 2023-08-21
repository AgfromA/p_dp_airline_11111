package app.config.security.util;

import app.config.security.domain.JwtAuthentication;
import app.entities.account.Role;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtUtils {


    public static JwtAuthentication generate(Claims claims) throws JsonProcessingException {
        final var jwtInfoToken = new JwtAuthentication();
        jwtInfoToken.setRoles(getRoles(claims));
        jwtInfoToken.setUsername(claims.getSubject());
        return jwtInfoToken;
    }

    private static Set<Role> getRoles(Claims claims) throws JsonProcessingException {

        var mapper = new ObjectMapper();
        TypeReference<List<Role>> tr = new TypeReference<>(){};
        var jsonRole = mapper.writeValueAsString(claims.get("roles", List.class));
        return Set.copyOf(mapper.readValue(jsonRole, tr));

    }

}
