package com.uit.hotelmanagement.exceptions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.parsing.Problem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handlerResourceNotFoundException(ResourceNotFoundException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)// ngoại lệ từ @Column(unique = true)
    public ProblemDetail handlerSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex) {
        final String EMAIL_CONSTRAINT = "UKrfbvkrffamfql7cjmen8v976v";
        final String PHONE_CONSTRAINT = "UK6v6x92wb400iwh6unf5rwiim4";
        final String ROOM_CONSTRAINT = "UK7ljglxlj90ln3lbas4kl983m2";
        final String SERVICE_CONSTRAINT = "unique_serviceName";

        String errorMessage = ex.getMessage();
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);

        if (errorMessage.contains(EMAIL_CONSTRAINT)) {
            problemDetail.setDetail("This email already in use");
        } else if (errorMessage.contains(PHONE_CONSTRAINT)) {
            problemDetail.setDetail("This phone number already in use");
        } else if (errorMessage.contains(ROOM_CONSTRAINT)) {
            problemDetail.setDetail("This room number already exists");
        } else if (errorMessage.contains(SERVICE_CONSTRAINT)) {
            problemDetail.setDetail("This service name already exists");
        } else {
            problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "An unknown constraint violation occurred");
        }

        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)// ngoại lệ từ @Valid của các DTO
    public ProblemDetail handlerMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        // Create problemDetail with status and detail
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "One or more fields are not valid!");

        // Create map to store the errors
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();

            errors.put(fieldName, errorMessage);
        });

        // Add errors into problemDetail, setProperty allows to add the custom field
        problemDetail.setProperty("List of errors", errors);

        return problemDetail;
    }

    @ExceptionHandler(FileServiceException.class)
    public ProblemDetail handlerFileServiceException(FileServiceException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)// ngoại lệ từ các annotation validation của các entity
    public ProblemDetail handleConstraintViolationException(ConstraintViolationException ex) {

        Map<String, String> errors = new HashMap<>();

        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();

        violations.forEach(violation -> {
            String propertyPath = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(propertyPath, errorMessage);
        });

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "One or more fields violated the constraint!");
        problemDetail.setProperty("List of constraint violations", errors);

        return problemDetail;
    }

    @ExceptionHandler(NumberFormatException.class)
    public ProblemDetail handlerNumberFormatException(NumberFormatException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage() + " is invalid, input must be a number");
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ProblemDetail handlerDateTimeParseException(DateTimeParseException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(CustomLocalDateException.class)
    public ProblemDetail handlerCustomLocalDateException(CustomLocalDateException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(RoomNotAvailableException.class)
    public ProblemDetail handlerRoomNotAvailableException(RoomNotAvailableException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(BookingStatusException.class)
    public ProblemDetail handlerBookingStatusException(BookingStatusException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(RefreshTokenException.class)
    public ProblemDetail handlerRefreshTokenException(RefreshTokenException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }
}
