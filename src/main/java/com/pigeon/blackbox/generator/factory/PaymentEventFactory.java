package com.pigeon.blackbox.generator.factory;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.pigeon.blackbox.domain.enums.EventType;
import com.pigeon.blackbox.domain.enums.PaymentStatus;
import com.pigeon.blackbox.domain.enums.Plan;
import com.pigeon.blackbox.domain.model.PaymentEvent;
import com.pigeon.blackbox.generator.random.UniformPicker;

@Component 
@Profile("generate")
public class PaymentEventFactory implements EventFactory {
    private final UniformPicker uniformPicker;
    private static final PaymentStatus[] PAYMENT_STATUSES = PaymentStatus.values();
    private static final Plan[] PAYMENT_PLANS = Plan.values(); 
    private static final String CURRENCY = "EUR";

    public PaymentEventFactory(UniformPicker uniformPicker) {
        this.uniformPicker = uniformPicker; 
    }

    @Override
    public EventType type() { return EventType.PAYMENT;}

    @Override
    public PaymentEvent create(Integer userId, Instant timestamp) {
        PaymentStatus status = uniformPicker.randomOf(PAYMENT_STATUSES); 
        Plan plan = uniformPicker.randomOf(PAYMENT_PLANS);

        Integer amount; 
        Instant expirationDate; 

        if (status == PaymentStatus.SUCCESS) {
            expirationDate = timestamp.plus(30, ChronoUnit.DAYS);
            
            amount = switch (plan) {
                case PRO -> 999;
                case FAMILY -> 1999; 
            };  
        }
        else {
            expirationDate = null;
            amount = null; 
        }

        return new PaymentEvent(userId, timestamp, status, plan, expirationDate, amount, CURRENCY);
    }
}
