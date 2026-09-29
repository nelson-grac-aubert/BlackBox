package com.pigeon.blackbox.domain.model;

import java.time.Instant;

import org.springframework.data.annotation.TypeAlias;

import com.pigeon.blackbox.domain.enums.EventType;
import com.pigeon.blackbox.domain.enums.PaymentStatus;
import com.pigeon.blackbox.domain.enums.Plan;

/* TypeAlias : a Spring tag to know which Event child class to instantiate 
from Mongo, as Event is abstract */
@TypeAlias("PAYMENT")
public class PaymentEvent extends Event {
    private PaymentStatus paymentStatus;

    private Plan plan;

    private Instant expirationDate;

    private Integer amount;

    private String currency;

    public PaymentEvent(Integer userId, Instant timestamp, PaymentStatus paymentStatus, Plan plan, Instant expirationDate, Integer amount, String currency) {
        super(EventType.PAYMENT, userId, timestamp);
        this.paymentStatus = paymentStatus;
        this.plan = plan;
        this.expirationDate = expirationDate;
        this.amount = amount;
        this.currency = currency;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public Plan getPlan() {
        return plan;
    }

    public Instant getExpirationDate() {
        return expirationDate;
    }

    public Integer getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }
}
