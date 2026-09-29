package com.pigeon.blackbox.domain.model;

import java.time.Instant;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.pigeon.blackbox.domain.enums.EventType;

// All subclasses share this collection
@Document(collection = "events") 
// abstract : we don't create "just an Event", we need a type and specific fields
public abstract class Event {
    @Id 
    private ObjectId eventId; 

    // Integer over int : can be null and checked with Spring Validation 
    private Integer userId; 

    // Instant : UTC date formatted into BSON for Mongo
    private Instant timestamp; 

    private EventType eventType;

    protected Event(EventType eventType, Integer userId, Instant timestamp) {
        this.eventType = eventType;
        this.userId = userId;
        this.timestamp = timestamp;
    }

    public ObjectId getEventId() { 
        return eventId;
    }

    public Integer getUserId() {
        return userId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public EventType getEventType() {
        return eventType;
    }
}
