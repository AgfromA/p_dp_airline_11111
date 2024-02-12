package app.exceptions;

import org.springframework.http.HttpStatus;

public class DuplicateFieldException extends BusinessException {

    public DuplicateFieldException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}