package app.controllers.api;

import app.controllers.domain.JwtRequest;
import app.controllers.domain.JwtResponse;
import app.controllers.domain.RefreshJwtRequest;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.Hidden;
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
    @ApiOperation(value = "Login and get token")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Got tokens"),
            @ApiResponse(code = 500, message = "Authentication exception")
    })
    ResponseEntity<JwtResponse> login(
            @ApiParam(
                    name = "request",
                    value = "JwtRequest model"
            )
            @RequestBody JwtRequest authRequest) throws AuthException;

    @PostMapping("/token")
    @ApiOperation(value = "Get new access token")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Got an access token"),
            @ApiResponse(code = 401, message = "Mismatch refresh token")
    })
    ResponseEntity<JwtResponse> getNewAccessToken(
            @ApiParam(
                    name = "request",
                    value = "RefreshJwtRequest model"
            )
            @RequestBody RefreshJwtRequest request) throws AuthException;

    @PostMapping("/refresh")
    @ApiOperation(value = "Get new token")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Got tokens"),
            @ApiResponse(code = 401, message = "Mismatch refresh token")
    })
    ResponseEntity<JwtResponse> getNewRefreshToken(
            @ApiParam(
                    name = "request",
                    value = "RefreshJwtRequest model"
            )
            @RequestBody RefreshJwtRequest request) throws AuthException;

    @Hidden
    @GetMapping("/login")
    @ApiOperation(value = "Get login page")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Got login page"),
            @ApiResponse(code = 401, message = "Not enough rights")
    })
    String loginPage(
            @ApiParam(
                    name = "request",
                    value = "HttpServletRequest"
            )
            HttpServletRequest request);
}