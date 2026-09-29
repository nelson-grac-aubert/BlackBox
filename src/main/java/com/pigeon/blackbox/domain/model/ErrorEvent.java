package com.pigeon.blackbox.domain.model;

import java.time.Instant;

import org.springframework.data.annotation.TypeAlias;

import com.pigeon.blackbox.domain.enums.EventType;

/* TypeAlias : a Spring tag to know which Event child class to instantiate 
from Mongo, as Event is abstract */
@TypeAlias("ERROR")
public class ErrorEvent extends Event {

    public ErrorEvent(Integer userId, Instant timestamp) {
        super(EventType.ERROR, userId, timestamp);
    }
}
