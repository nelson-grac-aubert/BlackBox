package com.pigeon.blackbox.domain.model;

import java.time.Instant;

import org.springframework.data.annotation.TypeAlias;

import com.pigeon.blackbox.domain.enums.EventType;

/* TypeAlias : a Spring tag to know which Event child class to instantiate 
from Mongo, as Event is abstract */
@TypeAlias("API_CALL")
public class ApiCallEvent extends Event {

    public ApiCallEvent(Integer userId, Instant timestamp) {
        super(EventType.API_CALL, userId, timestamp);
    }
}
