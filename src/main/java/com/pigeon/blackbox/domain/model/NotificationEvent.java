package com.pigeon.blackbox.domain.model;

import java.time.Instant;

import org.springframework.data.annotation.TypeAlias;

import com.pigeon.blackbox.domain.enums.NotificationChannel;
import com.pigeon.blackbox.domain.enums.EventType;
import com.pigeon.blackbox.domain.enums.NotificationStatus;
import com.pigeon.blackbox.domain.enums.NotificationTemplate;

/* TypeAlias : a Spring tag to know which Event child class to instantiate 
from Mongo, as Event is abstract */
@TypeAlias("NOTIFICATION")
public class NotificationEvent extends Event {
    private NotificationStatus notificationStatus;

    private NotificationTemplate template;

    private NotificationChannel channel;

    public NotificationEvent(Integer userId, Instant timestamp, NotificationStatus notificationStatus, NotificationTemplate template, NotificationChannel channel) {
        super(EventType.NOTIFICATION, userId, timestamp);
        this.notificationStatus = notificationStatus;
        this.template = template;
        this.channel = channel;
    }

    public NotificationStatus getNotificationStatus() {
        return notificationStatus;
    }

    public NotificationTemplate getTemplate() {
        return template;
    }

    public NotificationChannel getChannel() {
        return channel;
    }
}
