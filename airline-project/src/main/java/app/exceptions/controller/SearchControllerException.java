package app.exceptions.controller;

import org.springframework.http.HttpStatus;

public class SearchControllerException extends AbstractControllerException {

    public SearchControllerException(String message, HttpStatus httpStatus ) {
        super(message, httpStatus);
    }
}
