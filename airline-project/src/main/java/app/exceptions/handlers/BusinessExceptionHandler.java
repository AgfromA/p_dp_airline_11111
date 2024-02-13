package app.exceptions.handlers;

import app.exceptions.BusinessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BusinessExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<String> handleSearchException(BusinessException e) {
        return new ResponseEntity<>(e.getMessage(), e.getHttpStatus());
    }
}