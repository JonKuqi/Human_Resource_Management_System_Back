package com.hrms.Human_Resource_Management_System_Back.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for handling data integrity violations.
 * <p>
 * This class catches {@link DataIntegrityViolationException} thrown by the application and returns a meaningful error response.
 * It customizes the error message depending on the constraint violation (e.g., duplicate email or username).
 * </p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles {@link DataIntegrityViolationException} and provides a custom error message.
     * <p>
     * This method checks the specific constraint that was violated (e.g., email or username) and returns a 409 Conflict status
     * with an appropriate error message.
     * </p>
     *
     * @param ex the {@link DataIntegrityViolationException} to handle
     * @return a {@link ResponseEntity} containing a map with the error message
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String message = "Duplicate entry. This email or username is already in use.";

        if (ex.getCause() instanceof org.hibernate.exception.ConstraintViolationException cause) {
            if (cause.getConstraintName().contains("user_email_key")) {
                message = "Email is already registered.";
            } else if (cause.getConstraintName().contains("user_username_key")) {
                message = "Username is already taken.";
            }
        }

        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error); // 409 Conflict
    }
}