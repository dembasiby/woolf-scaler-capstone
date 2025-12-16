package com.dembasiby.productservice.dto.error;

import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Map;

public class ApiError {

    public ApiError(
            HttpStatus status,
            String message,
            String path
    ) {
        int status1 = status.value();
        String error = status.getReasonPhrase();
        Instant timestamp = Instant.now();
    }

    public ApiError(
            HttpStatus status,
            String message,
            String path,
            Map<String, String> validationErrors
    ) {
        this(status, message, path);
        // Optional
    }
}
