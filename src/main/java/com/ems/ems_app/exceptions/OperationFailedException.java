package com.ems.ems_app.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // Or BAD_REQUEST depending on cause
public class OperationFailedException extends RuntimeException {
    public OperationFailedException(String message) {
        super(message);
    }
    public OperationFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}