package app.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class SearchControllerException extends RuntimeException {

    @Getter
    private final HttpStatus httpStatus;

    public SearchControllerException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}