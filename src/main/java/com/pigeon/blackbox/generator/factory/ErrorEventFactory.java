package com.pigeon.blackbox.generator.factory;

import java.time.Instant;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.pigeon.blackbox.domain.enums.ErrorType;
import com.pigeon.blackbox.domain.enums.EventType;
import com.pigeon.blackbox.domain.enums.Severity;
import com.pigeon.blackbox.domain.model.ErrorEvent;
import com.pigeon.blackbox.domain.model.Event;
import com.pigeon.blackbox.generator.random.UniformPicker;

@Component
@Profile("generate")
public class ErrorEventFactory implements EventFactory {

    private final UniformPicker uniformPicker;
    private static final ErrorType[] ERROR_TYPES = ErrorType.values();
    private static final Severity[] SEVERITIES = Severity.values();
    private static final Integer[] ERROR_CODES = {400, 401, 403, 500, 503, 504}; 
    private static final String[] MESSAGES = {
        "Connection timed out", 
        "Invalid or expired token", 
        "Database unavailable", 
        "Unexpected null value"
    };
    private static final String STACK_TRACE = "at com.pigeon.service.MessageService.send(MessageService.java:42)";


    public ErrorEventFactory(UniformPicker uniformPicker) {
        this.uniformPicker = uniformPicker;
    }

    @Override
    public EventType type() { return EventType.ERROR; }

    @Override
    public Event create(Integer userId, Instant timestamp) {
        ErrorType errorType = uniformPicker.randomOf(ERROR_TYPES);
        Severity severity = uniformPicker.randomOf(SEVERITIES);
        Integer errorCode = uniformPicker.randomOf(ERROR_CODES); 
        String errorMessage = uniformPicker.randomOf(MESSAGES); 

        return new ErrorEvent(userId, timestamp, errorType, severity, errorCode, errorMessage, STACK_TRACE);
    }
}
