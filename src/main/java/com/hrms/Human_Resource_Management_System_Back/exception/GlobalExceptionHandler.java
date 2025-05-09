package com.hrms.Human_Resource_Management_System_Back.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

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