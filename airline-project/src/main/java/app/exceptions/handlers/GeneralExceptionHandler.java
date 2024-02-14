package app.exceptions.handlers;

import app.dto.ExceptionResponseDto;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.TraceContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.cloud.sleuth.Tracer;

import java.sql.SQLException;
import java.util.Optional;

@ControllerAdvice
public class GeneralExceptionHandler {

    @Autowired
    private Tracer tracer;

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponseDto> handleIllegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<>(new ExceptionResponseDto(e.getMessage(), getRequestId()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponseDto> handleValidationException(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(new ExceptionResponseDto("Validation failed for parameter " + e.getParameter().getParameterName(), "400"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionResponseDto> handleConstraintViolationException(ConstraintViolationException e) {
        return new ResponseEntity<>(new ExceptionResponseDto(e.getMessage(), getRequestId()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponseDto> handleRuntimeException(RuntimeException e) {
        return new ResponseEntity<>(new ExceptionResponseDto(e.getMessage(), getRequestId()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ExceptionResponseDto> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return new ResponseEntity<>(new ExceptionResponseDto(e.getMessage(), getRequestId()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ExceptionResponseDto> handleException(SQLException e) {
        return new ResponseEntity<>(new ExceptionResponseDto(e.getMessage(), getRequestId()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ExceptionResponseDto> handleException(BindException e) {
        return new ResponseEntity<>(new ExceptionResponseDto(e.getMessage(), getRequestId()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponseDto> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return new ResponseEntity<>(new ExceptionResponseDto(e.getMessage(), getRequestId()), HttpStatus.BAD_REQUEST);
    }

    /**
     * Достает traceId, перехватываемый из загловка x-b3-traceid или генерируемый Spring Cloud Sleuth
     */
    private String getRequestId() {
        return Optional.ofNullable(tracer.currentSpan())
                .map(Span::context)
                .map(TraceContext::traceId)
                .orElse("No request ID");
    }
}