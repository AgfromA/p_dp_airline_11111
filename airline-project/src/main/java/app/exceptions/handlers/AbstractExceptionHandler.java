package app.exceptions.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.TraceContext;
import org.springframework.cloud.sleuth.Tracer;

import java.util.Optional;

/**
 * Наследники этого класса перехватывают ошибки, возникающие во время работы приложения
 * Отдают их клиенту в обработанном виде с нужным статус-кодом
 */
public abstract class AbstractExceptionHandler {

    @Autowired
    private Tracer tracer;

    /**
     * Достает traceId, перехватываемый из загловка x-b3-traceid или генерируемый Spring Cloud Sleuth
     */
    protected String getRequestId() {
        return Optional.ofNullable(tracer.currentSpan())
                .map(Span::context)
                .map(TraceContext::traceId)
                .orElse("No request ID");
    }
}