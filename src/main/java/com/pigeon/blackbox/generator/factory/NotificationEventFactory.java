package com.pigeon.blackbox.generator.factory;

import java.time.Instant;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.pigeon.blackbox.domain.enums.EventType;
import com.pigeon.blackbox.domain.enums.NotificationChannel;
import com.pigeon.blackbox.domain.enums.NotificationStatus;
import com.pigeon.blackbox.domain.enums.NotificationTemplate;
import com.pigeon.blackbox.domain.model.Event;
import com.pigeon.blackbox.domain.model.NotificationEvent;
import com.pigeon.blackbox.generator.random.UniformPicker;

@Component 
@Profile("generate")
public class NotificationEventFactory implements EventFactory {

    private final UniformPicker uniformPicker;
    private static final NotificationStatus[] NOTIFICATION_STATUSES = NotificationStatus.values();
    private static final NotificationTemplate[] NOTIFICATION_TEMPLATES = NotificationTemplate.values();
    private static final NotificationChannel[] NOTIFICATION_CHANNELS = NotificationChannel.values();

    public NotificationEventFactory(UniformPicker uniformPicker) {
        this.uniformPicker = uniformPicker; 
    }  

    @Override
    public EventType type() { return EventType.NOTIFICATION; }

    @Override 
    public Event create(Integer userId, Instant timestamp) { 
        NotificationStatus status = uniformPicker.randomOf(NOTIFICATION_STATUSES); 
        NotificationTemplate template = uniformPicker.randomOf(NOTIFICATION_TEMPLATES); 
        NotificationChannel channel = uniformPicker.randomOf(NOTIFICATION_CHANNELS); 

        return new NotificationEvent(userId, timestamp, status, template, channel); 
    }
}
