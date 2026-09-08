package com.enviro.assessment.junior.candidate.exception;

import java.time.LocalDateTime;

/**
 * Consistent JSON shape for every error response the API returns,
 * regardless of what went wrong. This is what the assessment brief's
 * example error response is modeled on:
 * { "timestamp", "status", "error", "message", "path" }
 */
public class ErrorResponse {

    private final LocalDateTime timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final String path;

    public ErrorResponse(int status, String error, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }
}
