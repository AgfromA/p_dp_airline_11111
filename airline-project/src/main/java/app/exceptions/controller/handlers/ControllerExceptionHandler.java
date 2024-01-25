package app.exceptions.controller.handlers;

import app.exceptions.controller.AbstractControllerException;
import app.exceptions.controller.SearchControllerException;
import app.exceptions.controller.responses.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
@AllArgsConstructor
public class ControllerExceptionHandler {
    private HttpServletRequest request;

    /**
     * Ловит конкретный Exception
     */
    @ExceptionHandler({SearchControllerException.class})
    ResponseEntity<ErrorResponse> handleSearchException(SearchControllerException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(createErrorResponse(e));
    }

    /**
     * Ловит абстрактный ControllerException, если выше нет метода для дочернего
     */
    @ExceptionHandler({AbstractControllerException.class})
    ResponseEntity<ErrorResponse> handleOtherException(AbstractControllerException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(createErrorResponse(e));

    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse(e));
    }

    ErrorResponse createErrorResponse(Exception e) {
        return new ErrorResponse(e, getRequestUrl());
    }

    private String getRequestUrl() {
        return request.getRequestURL().toString();
    }


    // Ловит IllegalArgumentException, если page < 0 или size < 1
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
