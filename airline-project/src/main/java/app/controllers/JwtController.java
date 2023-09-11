package app.controllers;

import app.config.security.jwt.domain.JwtRequest;
import app.config.security.jwt.domain.JwtResponse;
import app.config.security.jwt.domain.RefreshJwtRequest;
import app.config.security.jwt.service.AuthService;
import app.controllers.api.JwtControllerApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.message.AuthException;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
public class JwtController implements JwtControllerApi {

    private final AuthService authService;

    @Override
    public ResponseEntity<JwtResponse> login(JwtRequest authRequest) throws AuthException {
        final var token = authService.login(authRequest);
        return ResponseEntity.ok(token);
    }

    @Override
    public ResponseEntity<JwtResponse> getNewAccessToken(RefreshJwtRequest request) throws AuthException {
        final var token = authService.getAccessToken(request.getRefreshToken());
        if (token.getAccessToken() != null) {
            log.info("getNewAccessToken: access token was generated");
            return ResponseEntity.ok(token);
        } else {
            log.info("getNewAccessToken: this refresh token doesn't exist");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public ResponseEntity<JwtResponse> getNewRefreshToken(RefreshJwtRequest request) throws AuthException {
        final var token = authService.refresh(request.getRefreshToken());
        if (token.getAccessToken() != null) {
            log.info("getNewRefreshToken: new tokens were generated");
            return ResponseEntity.ok(token);
        } else {
            log.info("getNewRefreshToken: this refresh token doesn't exist");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public String loginPage(HttpServletRequest request) {
        var referrer = request.getHeader("Referer");
        if (referrer != null) {
            request.getSession().setAttribute("url_prior_login", referrer);
        }
        return "login";
    }
}