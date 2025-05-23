package com.hrms.Human_Resource_Management_System_Back.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for subscription-related runtime exceptions.
 * <p>
 * This class handles runtime exceptions thrown during subscription operations,
 * particularly when the maximum user limit is exceeded.
 * </p>
 * <p>
 * If the exception message contains the phrase "maximum user limit", an additional flag
 * <code>upgradeSuggestion: true</code> is included in the response.
 * </p>
 */
@RestControllerAdvice
public class SubscriptionLimitExceptionHandler {
    /**
     * Handles general runtime exceptions and formats a custom error response.
     *
     * @param ex the caught {@link RuntimeException}
     * @return a {@link ResponseEntity} containing an error message, HTTP status, and
     * optionally a suggestion to upgrade the plan
     */
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
