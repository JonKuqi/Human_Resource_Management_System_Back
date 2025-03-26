package com.hrms.Human_Resource_Management_System_Back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Demo controller for testing secured endpoints.
 * <p>
 * This controller provides a simple GET endpoint that returns a greeting message.
 * </p>
 */
@RestController
@RequestMapping("/api/demo-controller")
public class demoController {

    /**
     * Handles GET requests and returns a greeting message.
     *
     * @return a {@link ResponseEntity} containing a greeting message
     */
    @GetMapping
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hello from secured Endpoint");
    }
}