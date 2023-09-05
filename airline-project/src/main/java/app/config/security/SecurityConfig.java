package app.config.security;

import app.config.security.jwt.filter.JwtFilter;
import app.util.LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Profile("!noSecurity")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final LoginSuccessHandler successHandler;
    private final UserDetailsService userDetailsService;
    private final JwtFilter jwtFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .httpBasic().disable()
                .authorizeRequests()
                .antMatchers("/admin").hasRole("ADMIN")
                .antMatchers("/passenger").hasRole("PASSENGER")
                .antMatchers("/manager").hasRole("MANAGER")
                .antMatchers("/api/auth/login", "/api/auth/token").permitAll()
                .antMatchers(HttpMethod.GET, "/api/aircrafts/**", "/api/destinations/**", "/api/flights/**",
                         "/api/flight_seats/**", "/api/seats/**","/api/payments/**","/api/example/**").permitAll()
                .antMatchers(HttpMethod.POST,"/api/payments","/api/example/").authenticated()
                .antMatchers(HttpMethod.PATCH,"/api/example/**").permitAll()
                .antMatchers(HttpMethod.DELETE,"/api/example/**").permitAll()
                .antMatchers("/api/search/**").permitAll()
                .antMatchers("/api/**").hasRole("ADMIN")
                .antMatchers("/email/**").permitAll()
                .antMatchers("/403").permitAll()
                .anyRequest().permitAll()
                .and()
                .exceptionHandling().authenticationEntryPoint(new Http403ForbiddenEntryPoint())
                .and()
                .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .successHandler(successHandler)
                    .permitAll()
                    .and()
                .logout()
                    .logoutSuccessUrl("/")
                    .permitAll();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(getEncoder());
    }

    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }
}
