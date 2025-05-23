package com.hrms.Human_Resource_Management_System_Back.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class SubscriptionLimitExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntime(RuntimeException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("message", ex.getMessage());
        error.put("status", 400);

        if (ex.getMessage().toLowerCase().contains("maximum user limit")) {
            error.put("upgradeSuggestion", true);
        }

        return ResponseEntity.badRequest().body(error);
    }
}
