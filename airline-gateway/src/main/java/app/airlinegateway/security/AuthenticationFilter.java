package app.airlinegateway.security;

import app.airlinegateway.security.exceptions.AuthorizationException;
import app.security.JwtProvider;
import app.security.JwtProviderLite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;

@Component
@Scope("singleton")
public class AuthenticationFilter extends AbstractGatewayFilterFactory<Object> {

    @Autowired
    private RouterValidator validator;
    @Autowired
    private JwtProviderLite jwtProvider;

    @Override
    public GatewayFilter apply(Object config) {
        return (ServerWebExchange exchange, GatewayFilterChain chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if (validator.isSecured(request)) {
                if (authExist(request)) {
                    final String token = extractToken(request);
                    List<String> roles = jwtProvider.extractRoles(token).orElseThrow(() -> new AuthorizationException("Error extract roles from token", HttpStatus.UNAUTHORIZED));
                    if (validator.needAuthority(request) && !validator.authorize(request, roles)) {
                        onError("Access denied", HttpStatus.FORBIDDEN);
                    }
                } else {
                    onError("There is no authorization header", HttpStatus.UNAUTHORIZED);
                }
            }
            return chain.filter(exchange);
        };
    }

    private String extractToken(ServerHttpRequest request) {
        return request.getHeaders().getOrEmpty("Authorization").get(0)
                .substring(7);
    }

    private void onError(String message, HttpStatus httpStatus) {
        throw new AuthorizationException(message, httpStatus);
    }

    private boolean authExist(ServerHttpRequest request) {
        return request.getHeaders().containsKey("Authorization");
    }


}
