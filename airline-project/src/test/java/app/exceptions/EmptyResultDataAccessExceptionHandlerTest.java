package app.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmptyResultDataAccessExceptionHandlerTest {

    @Test
    void emptyResultDataAccessExceptionHandlerTest() {
        var validationExceptionHandler = new ValidationExceptionHandler();
        var errorMessage = "EmptyResultDataAccess exception";
        var response = validationExceptionHandler
                .handleEmptyResultDataAccessException(new EmptyResultDataAccessException("EmptyResultDataAccess exception",1));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().getExceptionMessage().contains(errorMessage));
    }
}

