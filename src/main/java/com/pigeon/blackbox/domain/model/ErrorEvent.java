package com.pigeon.blackbox.domain.model;

import java.time.Instant;

import org.springframework.data.annotation.TypeAlias;

import com.pigeon.blackbox.domain.enums.ErrorType;
import com.pigeon.blackbox.domain.enums.EventType;
import com.pigeon.blackbox.domain.enums.Severity;

/* TypeAlias : a Spring tag to know which Event child class to instantiate 
from Mongo, as Event is abstract */
@TypeAlias("ERROR")
public class ErrorEvent extends Event {
    private ErrorType errorType;

    private Severity severity;

    private Integer errorCode;

    private String message;

    private String stackTrace;

    public ErrorEvent(Integer userId, Instant timestamp, ErrorType errorType, Severity severity, Integer errorCode, String message, String stackTrace) {
        super(EventType.ERROR, userId, timestamp);
        this.errorType = errorType;
        this.severity = severity;
        this.errorCode = errorCode;
        this.message = message;
        this.stackTrace = stackTrace;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public Severity getSeverity() {
        return severity;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }

    public String getStackTrace() {
        return stackTrace;
    }
}
