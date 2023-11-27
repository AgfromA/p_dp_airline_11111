package app.services;

import app.entities.account.Role;
import app.controllers.domain.JwtAuthentication;
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


//package app.services;
//
//import app.controllers.domain.JwtAuthentication;
//import app.entities.account.Role;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.PostConstruct;
//import java.security.Key;
//import java.util.Date;
//import java.util.List;
//import java.util.Set;
//
//@Service
//public class JwtUtils {
//    @Value("${jwt.secret}")
//    private String secret;
//
//    private Key key;
//
//    @PostConstruct
//    public void initKey() {
//        this.key = Keys.hmacShaKeyFor(secret.getBytes());
//    }
//
//    public Claims getClaims(String token) {
//        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
//    }
//
//    public boolean isExpired(String token) {
//        try {
//            return getClaims(token).getExpiration().before(new Date());
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    public static JwtAuthentication generate(Claims claims) throws JsonProcessingException {
//        final var jwtInfoToken = new JwtAuthentication();
//        jwtInfoToken.setRoles(getRoles(claims));
//        jwtInfoToken.setUsername(claims.getSubject());
//        return jwtInfoToken;
//    }
//
//    private static Set<Role> getRoles(Claims claims) throws JsonProcessingException {
//
//        var mapper = new ObjectMapper();
//        TypeReference<List<Role>> tr = new TypeReference<>(){};
//        var jsonRole = mapper.writeValueAsString(claims.get("roles", List.class));
//        return Set.copyOf(mapper.readValue(jsonRole, tr));
//
//    }
//}
