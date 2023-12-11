package app.controllers.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtResponse {

    private static final String JWT_TYPE = "Bearer";
    private String accessToken;
    private String refreshToken;
}