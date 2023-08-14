package app.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EntityNotFoundExceptionHandlerTest {

    @Test
    void entityNotFoundExceptionHandlerTest() {
        var validationExceptionHandler = new ValidationExceptionHandler();
        var errorMessage = "EntityNotFound exception";
        var response = validationExceptionHandler
                .handleEntityNotFoundException(new EntityNotFoundException("EntityNotFound exception"));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().getExceptionMessage().contains(errorMessage));
    }
}
