package app.exceptions.controller.responses;

import app.exceptions.controller.AbstractControllerException;
import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResponse {
    private Error error;

    public ErrorResponse(Exception e, String requestUrl) {
        this.setError(new ErrorResponse.Error(e.getMessage(), requestUrl));
    }
    @AllArgsConstructor
    @Getter
    public class Error {
        private String message;
        private String requestUrl;

    }
}
