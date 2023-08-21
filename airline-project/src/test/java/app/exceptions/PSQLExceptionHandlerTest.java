package app.exceptions;

import org.junit.jupiter.api.Test;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PSQLExceptionHandlerTest {

    @Test
    void PSQLExceptionHandlerTest() {
        var validationExceptionHandler = new ValidationExceptionHandler();
        var errorMessage = "PSQL exception";
        var response = validationExceptionHandler
                .handlePSQLException(new PSQLException(errorMessage, null));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().getExceptionMessage().contains(errorMessage));
    }


}
