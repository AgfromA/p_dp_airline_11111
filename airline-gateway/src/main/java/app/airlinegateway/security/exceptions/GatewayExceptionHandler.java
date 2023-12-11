package app.airlinegateway.security.exceptions;

import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

@Component
@Order(-2)
public class GatewayExceptionHandler implements WebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        if (ex instanceof AuthorizationException) {
            return handleAuthorizationException((AuthorizationException) ex, exchange);
        }
        // Обрабатываем другие исключения здесь

        // Если нет совпадения, передаем управление другим обработчикам или фильтрам
        return Mono.error(ex);
    }

    private Mono<Void> handleAuthorizationException(AuthorizationException ex, ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.setStatusCode(ex.getHttpStatus());

        String responseBody = "{\n\"error\": \"" + ex.getMessage() +"\"\n}";
        // Так записывается тело ответа =\
        return response.writeWith(
                Mono.just(response.bufferFactory().wrap(responseBody.getBytes()))
        );

    }
}
