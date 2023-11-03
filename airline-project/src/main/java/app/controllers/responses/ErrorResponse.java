package app.controllers.responses;

import app.exceptions.SearchRequestException;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResponse {
    private Error error;

    public ErrorResponse(SearchRequestException e) {
        ErrorResponse.Error error = new ErrorResponse.Error(e.getHttpStatus().value(),e.getHttpStatus(), e.getMessage());
        this.setError(error);
    }
    @AllArgsConstructor
    @Getter
    public class Error {
        private int code;
        private HttpStatus httpStatus;
        private String message;
    }
}
