package app.exceptions;

import org.springframework.http.HttpStatus;

public class SearchControllerException extends BusinessException {

    public SearchControllerException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}