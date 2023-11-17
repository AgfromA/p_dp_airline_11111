package app.exceptions.controller;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public abstract class AbstractControllerException extends RuntimeException {
    @Getter
    HttpStatus httpStatus;
    public AbstractControllerException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
