package app.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RuntimeExceptionHandlerTest {

    @Test
    void runtimeExceptionHandlerTest() {
        var validationExceptionHandler = new ValidationExceptionHandler();
        var runtimeException = new RuntimeException();
        var response = validationExceptionHandler.handleRuntimeException(runtimeException);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}

