package app.config.security.jwt.service;

import app.config.security.jwt.domain.JwtRequest;
import app.config.security.jwt.domain.JwtResponse;
import app.services.interfaces.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.message.AuthException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final String USER_NOT_FOUND_MESSAGE = "Пользователь не найден";

    private final AccountService accountService;
    private final Map<String, String> refreshStorage = new HashMap<>();
    private final JwtProvider jwtProvider;
    private final PasswordEncoder encoder;

    public JwtResponse login(@NonNull JwtRequest authRequest) throws AuthException {
        final var user = Optional.of(accountService.getAccountByEmail(authRequest.getUsername()))
                .orElseThrow(() -> new AuthException(USER_NOT_FOUND_MESSAGE));
        if (encoder.matches(authRequest.getPassword(), user.getPassword())) {
            final String accessToken = jwtProvider.generateAccessToken(user);
            final String refreshToken = jwtProvider.generateRefreshToken(user);
            refreshStorage.put(user.getEmail(), refreshToken);
            return new JwtResponse(accessToken, refreshToken);
        } else {
            throw new AuthException("Неправильный пароль");
        }
    }

    public JwtResponse getAccessToken(@NonNull String refreshToken) throws AuthException {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final var claims = jwtProvider.getRefreshClaims(refreshToken);
            final var login = claims.getSubject();
            final var saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final var user = Optional.of(accountService.getAccountByEmail(login))
                        .orElseThrow(() -> new AuthException(USER_NOT_FOUND_MESSAGE));
                final var accessToken = jwtProvider.generateAccessToken(user);
                return new JwtResponse(accessToken, null);
            }
        }
        return new JwtResponse(null, null);
    }

    public JwtResponse refresh(@NonNull String refreshToken) throws AuthException {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final var claims = jwtProvider.getRefreshClaims(refreshToken);
            final var login = claims.getSubject();
            final var saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final var user = Optional.of(accountService.getAccountByEmail(login))
                        .orElseThrow(() -> new AuthException(USER_NOT_FOUND_MESSAGE));
                final var accessToken = jwtProvider.generateAccessToken(user);
                final var newRefreshToken = jwtProvider.generateRefreshToken(user);
                refreshStorage.put(user.getEmail(), newRefreshToken);
                return new JwtResponse(accessToken, newRefreshToken);
            }
        }
        throw new AuthException("Невалидный JWT токен");
    }
}
