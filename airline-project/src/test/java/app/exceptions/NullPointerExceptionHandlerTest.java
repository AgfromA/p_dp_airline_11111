package app.exceptions;

import app.exceptions.dtos.ResponseExceptionDto;
import app.exceptions.handlers.ValidationExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NullPointerExceptionHandlerTest {

    @Test
    public void testHandleNullPointerException() {
        var handler = new ValidationExceptionHandler();
        String errorMessage = "Null pointer exception";
        ResponseEntity<ResponseExceptionDto> response = handler.handleNullPointerException(new NullPointerException(errorMessage));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().getExceptionMessage().contains(errorMessage));
    }
}
