package com.ensa.labs.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDetails {
    private final Date timestamp;
    private final String message;
    private final String details;
    /** Field name -> validation message, only for validation errors. */
    private final Map<String, String> errors;

    public ErrorDetails(Date timestamp, String message, String details) {
        this(timestamp, message, details, null);
    }

    public ErrorDetails(Date timestamp, String message, String details, Map<String, String> errors) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
        this.errors = errors;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
