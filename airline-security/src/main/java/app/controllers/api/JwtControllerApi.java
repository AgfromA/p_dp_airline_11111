package app.controllers.api;

import app.controllers.domain.JwtRequest;
import app.controllers.domain.JwtResponse;
import app.controllers.domain.RefreshJwtRequest;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.security.auth.message.AuthException;
import javax.servlet.http.HttpServletRequest;

@Api(tags = "JWT")
@Tag(name = "JWT", description = "Авторизация и операции с JWT")
@RequestMapping("/api/auth")
public interface JwtControllerApi {

    @PostMapping("/login")
    @Operation(summary = "Login and get token")
    ResponseEntity<JwtResponse> login(@RequestBody JwtRequest authRequest) throws AuthException;

    @PostMapping("/token")
    @Operation(summary = "Get new access token")
    ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request) throws AuthException;

    @PostMapping("/refresh")
    @Operation(summary = "Get new token")
    ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) throws AuthException;

    @Hidden
    @GetMapping("/login")
    @Operation(summary = "Get login page")
    String loginPage(@Parameter(description = "request") HttpServletRequest request);
}