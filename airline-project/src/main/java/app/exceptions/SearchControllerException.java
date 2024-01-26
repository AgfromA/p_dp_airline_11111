package app.exceptions;

import org.springframework.http.HttpStatus;

public class SearchControllerException extends AbstractControllerException {

    public SearchControllerException(String message, HttpStatus httpStatus ) {
        super(message, httpStatus);
    }
}