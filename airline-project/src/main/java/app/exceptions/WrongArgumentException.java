package app.exceptions;

import org.springframework.http.HttpStatus;

public class WrongArgumentException extends BusinessException {

    public WrongArgumentException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}