package com.pigeon.blackbox.generator.factory;

import java.time.Instant;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.pigeon.blackbox.domain.enums.DeviceType;
import com.pigeon.blackbox.domain.enums.EventType;
import com.pigeon.blackbox.domain.enums.LoginStatus;
import com.pigeon.blackbox.domain.model.Device;
import com.pigeon.blackbox.domain.model.Event;
import com.pigeon.blackbox.domain.model.LoginEvent;
import com.pigeon.blackbox.generator.random.UniformPicker;

@Component 
@Profile("generate")
public class LoginEventFactory implements EventFactory {

    private final UniformPicker uniformPicker;
    private static final DeviceType[] DEVICE_TYPES = DeviceType.values(); 
    private static final String[] OPERATING_SYSTEMS = {"Windows 11", "macOS", "iOS", "Android", "Linux"};
    private static final String[] BROWSERS = {"Safari", "Opera", "Chrome", "Internet Explorer"};
    private static final LoginStatus[] STATUSES = LoginStatus.values();
    private static final String[] IP_ADDRESSES = {"192.0.2.14", "192.0.2.87", "192.0.2.203", "198.51.100.7", "198.51.100.42", "198.51.100.156", "198.51.100.231", "203.0.113.9", "203.0.113.68", "203.0.113.190"};

    public LoginEventFactory(UniformPicker uniformPicker) {
        this.uniformPicker = uniformPicker; 
    }

    @Override
    public EventType type() { return EventType.LOGIN; }

    @Override 
    public Event create(Integer userId, Instant timestamp) { 

        DeviceType deviceType = uniformPicker.randomOf(DEVICE_TYPES); 
        String operatingSystem = uniformPicker.randomOf(OPERATING_SYSTEMS); 
        String browsers = uniformPicker.randomOf(BROWSERS); 

        Device device = new Device(deviceType, operatingSystem, browsers);

        LoginStatus loginStatus = uniformPicker.randomOf(STATUSES);
        String ipAddress = uniformPicker.randomOf(IP_ADDRESSES);

        return new LoginEvent(userId, timestamp, loginStatus, ipAddress, device);
    }
}
