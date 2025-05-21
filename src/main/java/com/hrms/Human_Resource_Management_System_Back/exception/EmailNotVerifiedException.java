package com.hrms.Human_Resource_Management_System_Back.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Exception thrown when an operation requires a verified email but the user's email is not verified.
 */
public class EmailNotVerifiedException extends AuthenticationException {
    private static final long serialVersionUID = 1L;

    public EmailNotVerifiedException(String message) {
        super(message);
    }
}