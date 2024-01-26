package app.exceptions.handlers;

import app.exceptions.EntityNotFoundException;
import app.exceptions.FlightSeatIsBookedException;
import app.exceptions.dtos.ResponseExceptionDto;
import org.postgresql.util.PSQLException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler({NullPointerException.class})
    public ResponseEntity<ResponseExceptionDto> handleNullPointerException(NullPointerException ex) {
        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage());
        var nullPointerExceptionDto = new ResponseExceptionDto(errors.toString(), LocalDateTime.now()); // тут изменено
        return new ResponseEntity<>(nullPointerExceptionDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({PSQLException.class})
    public ResponseEntity<ResponseExceptionDto> handlePSQLException(PSQLException ex) {
        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage());
        var pSqlExceptionDto = new ResponseExceptionDto(errors.toString(), LocalDateTime.now());
        return new ResponseEntity<>(pSqlExceptionDto, HttpStatus.BAD_REQUEST);
    }

    //    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<ResponseExceptionDto> handleRuntimeException(RuntimeException exception) {
        var runtimeExceptionDTO = new ResponseExceptionDto(exception.getMessage(), LocalDateTime.now());
        if (exception instanceof HttpMessageNotReadableException) {
            return new ResponseEntity<>(runtimeExceptionDTO, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(runtimeExceptionDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ResponseExceptionDto> handleConstraintViolation(ConstraintViolationException ex) {
        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getMessage());
        }
        var constraintViolationExceptionDto = new ResponseExceptionDto(errors.toString(), LocalDateTime.now());// тут изменено
        return new ResponseEntity<>(constraintViolationExceptionDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({BindException.class})
    public ResponseEntity<List<ResponseExceptionDto>> handleException(BindException exception) {
        var validationExceptionDto = bindFieldsExceptionsToList(exception, new ArrayList<>());
        return new ResponseEntity<>(validationExceptionDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({SQLException.class})
    public ResponseEntity<ResponseExceptionDto> handleException(SQLException exception) {
        var sqlExceptionDto = new ResponseExceptionDto(exception.getMessage(), LocalDateTime.now());// тут изменено
        if (exception.getSQLState().equals("23505")) {
            return new ResponseEntity<>(sqlExceptionDto, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(sqlExceptionDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<ResponseExceptionDto> handleEntityNotFoundException(EntityNotFoundException ex) {
        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage());
        var EntityNotFoundExceptionDto = new ResponseExceptionDto(errors.toString(), LocalDateTime.now());// тут изменено
        return new ResponseEntity<>(EntityNotFoundExceptionDto, HttpStatus.NOT_FOUND);
    }

    private List<ResponseExceptionDto> bindFieldsExceptionsToList(
            BindException e,
            List<ResponseExceptionDto> entityFieldsErrorList) {
        e.getFieldErrors().stream().forEach(a -> {
            entityFieldsErrorList.add(new ResponseExceptionDto(a.getField() + " " + a.getDefaultMessage(), LocalDateTime.now()));
        });
        return entityFieldsErrorList;
    }

    @ExceptionHandler({EmptyResultDataAccessException.class})
    public ResponseEntity<ResponseExceptionDto> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex) {
        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage());
        var emptyResultDataAccessException = new ResponseExceptionDto(errors.toString(), LocalDateTime.now());// тут изменено
        return new ResponseEntity<>(emptyResultDataAccessException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({FlightSeatIsBookedException.class})
    public ResponseEntity<ResponseExceptionDto> handleFlightSeatIsBookedException(FlightSeatIsBookedException ex) {
        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage());
        var flightSeatIsBookedException = new ResponseExceptionDto(errors.toString(), LocalDateTime.now());
        return new ResponseEntity<>(flightSeatIsBookedException, HttpStatus.BAD_REQUEST);
    }
}