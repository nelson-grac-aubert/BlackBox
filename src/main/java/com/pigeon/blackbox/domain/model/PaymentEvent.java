package com.pigeon.blackbox.domain.model;

import java.time.Instant;

import org.springframework.data.annotation.TypeAlias;

import com.pigeon.blackbox.domain.enums.EventType;

/* TypeAlias : a Spring tag to know which Event child class to instantiate 
from Mongo, as Event is abstract */
@TypeAlias("PAYMENT")
public class PaymentEvent extends Event {

    public PaymentEvent(Integer userId, Instant timestamp) {
        super(EventType.PAYMENT, userId, timestamp);
    }
}
