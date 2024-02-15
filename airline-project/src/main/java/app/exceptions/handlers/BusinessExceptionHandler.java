package app.exceptions.handlers;

import app.dto.ExceptionResponseDto;
import app.exceptions.BusinessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BusinessExceptionHandler extends AbstractExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ExceptionResponseDto> handleSearchException(BusinessException e) {
        return new ResponseEntity<>(new ExceptionResponseDto(e.getMessage(), getRequestId()), e.getHttpStatus());
    }
}