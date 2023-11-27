package app.airlinegateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Set;

@Component
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
                    Set<String> roles = jwtProvider.extractRoles(token); // ниже пройдем только если извлеклись роли => валидный токен
                    if (validator.needAuthority(request)) {
                        if (!validator.authorize(request, roles)) {
                            return dropRequest(exchange, HttpStatus.FORBIDDEN);
                        }
                    }
                } else {
                    return dropRequest(exchange,HttpStatus.UNAUTHORIZED);
                }
            }
            return chain.filter(exchange);
        };
    }

    private String extractToken(ServerHttpRequest request) {
        return request.getHeaders().getOrEmpty("Authorization").get(0)
                .substring(7);
    }

    private Mono<Void> dropRequest(ServerWebExchange exchange, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    private boolean authExist(ServerHttpRequest request) {
        return request.getHeaders().containsKey("Authorization");
    }


}
