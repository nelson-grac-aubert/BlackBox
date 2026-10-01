package com.pigeon.blackbox.generator.factory;

import java.time.Instant;

import com.pigeon.blackbox.domain.enums.EventType;
import com.pigeon.blackbox.domain.model.Event;

public interface EventFactory {
    EventType type(); 

    Event create(Integer userId, Instant timestamp); 

}
