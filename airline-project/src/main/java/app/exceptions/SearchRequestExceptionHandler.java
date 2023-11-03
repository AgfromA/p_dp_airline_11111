package app.exceptions;

import app.controllers.responses.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class SearchRequestExceptionHandler {
    @ExceptionHandler({SearchRequestException.class})
    ResponseEntity<ErrorResponse> handleSearchException(SearchRequestException e) {
        return new ResponseEntity(new ErrorResponse(e),
                e.getHttpStatus());
    }

}
