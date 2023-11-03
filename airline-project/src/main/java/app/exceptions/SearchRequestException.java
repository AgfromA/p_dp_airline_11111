package app.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class SearchRequestException extends RuntimeException {
    @Getter
    private HttpStatus httpStatus;
    public SearchRequestException(String message, HttpStatus httpStatus ) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
