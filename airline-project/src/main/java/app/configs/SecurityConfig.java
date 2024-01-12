package app.configs;

import app.security.JwtProviderLite;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {
    @Bean
    PasswordEncoder getPasswordEndcoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    JwtProviderLite getJwtProvider(@Value("${jwt.secret.access}") String token) {
        return new JwtProviderLite(token);
    }
}
