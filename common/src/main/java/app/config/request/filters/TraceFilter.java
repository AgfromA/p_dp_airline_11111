package app.config.request.filters;

import org.slf4j.MDC;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TraceFilter extends OncePerRequestFilter {
    private final Tracer tracer;

    public TraceFilter(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Span span = tracer.currentSpan();
        String traceId = span.context().traceId();
        String spanId = span.context().spanId();

        // Добавляем трейс в лог
        MDC.put("traceId", traceId);
        MDC.put("spanId", spanId);

        // Добавляем трейс в заголовки запроса
        request.setAttribute("X-B3-TraceId", traceId);
        request.setAttribute("X-B3-SpanId", spanId);

        // Добавляем трейс в заголовки ответа
        response.addHeader("X-B3-TraceId", traceId);
        response.addHeader("X-B3-SpanId", spanId);

        filterChain.doFilter(request, response);
    }

}
