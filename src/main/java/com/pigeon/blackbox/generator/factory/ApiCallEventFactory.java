package com.pigeon.blackbox.generator.factory;

import java.time.Instant;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.pigeon.blackbox.domain.enums.EventType;
import com.pigeon.blackbox.domain.enums.HttpMethod;
import com.pigeon.blackbox.domain.model.ApiCallEvent;
import com.pigeon.blackbox.domain.model.Event;
import com.pigeon.blackbox.generator.random.UniformPicker;

@Component
@Profile("generate")
public class ApiCallEventFactory implements EventFactory {

    private final UniformPicker uniformPicker;
    private static final HttpMethod[] HTTP_METHODS = HttpMethod.values();
    private static final String[] ENDPOINTS = {"/api/messages", "/api/conversations", "/api/users/me", "/api/files", "/api/notifications"};
    private static final Integer[] STATUS_CODES = {200, 201, 400, 401, 404, 500};

    private static final int MIN_RESPONSE_TIME_MS = 20;
    private static final int MAX_RESPONSE_TIME_MS = 2000;


    public ApiCallEventFactory(UniformPicker uniformPicker) {
        this.uniformPicker = uniformPicker;
    }

    @Override
    public EventType type() { return EventType.API_CALL; }

    @Override
    public Event create(Integer userId, Instant timestamp) {
        HttpMethod httpMethod = uniformPicker.randomOf(HTTP_METHODS);
        String endpoint = uniformPicker.randomOf(ENDPOINTS);
        Integer statusCode = uniformPicker.randomOf(STATUS_CODES); 
        int responseTimeMs = uniformPicker.randomInt(MIN_RESPONSE_TIME_MS, MAX_RESPONSE_TIME_MS);

        return new ApiCallEvent(userId, timestamp, endpoint, responseTimeMs, statusCode, httpMethod);
    }
}
