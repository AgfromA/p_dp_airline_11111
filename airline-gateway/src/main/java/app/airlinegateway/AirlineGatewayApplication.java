package app.airlinegateway;

import app.security.JwtProviderLite;
import app.security.JwtProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AirlineGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(AirlineGatewayApplication.class, args);
    }

    @Bean
    JwtProviderLite getJwtProvider(@Value("${jwt.secret.access}") String token) {
        return new JwtProviderLite(token);
    }
}
