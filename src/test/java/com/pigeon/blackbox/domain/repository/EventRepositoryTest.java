package com.pigeon.blackbox.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.mongodb.test.autoconfigure.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

import com.pigeon.blackbox.domain.enums.DeviceType;
import com.pigeon.blackbox.domain.enums.EventType;
import com.pigeon.blackbox.domain.enums.LoginStatus;
import com.pigeon.blackbox.domain.model.Device;
import com.pigeon.blackbox.domain.model.Event;
import com.pigeon.blackbox.domain.model.LoginEvent;

// ISOLATED TEST COMMAND : .\mvnw.cmd test -Dtest=EventRepositoryTest

@DataMongoTest 
@ActiveProfiles("test")
class EventRepositoryTest {

    @Autowired // tell Spring to inject the repository implementation
    private EventRepository eventRepository;

    @BeforeEach
    void cleanDatabase() {
        eventRepository.deleteAll();
    }

    @Test
    void savedLoginEventIsReadAsLoginEvent() {

        // GIVEN : prepare the data

        // timestamp with no more numbers after milliseconds
        Instant now = Instant.now().truncatedTo(ChronoUnit.MILLIS);

        Device device = new Device(DeviceType.DESKTOP, "MACOS", "Safari");

        LoginEvent loginEvent = new LoginEvent(1, now, LoginStatus.SUCCESS, "an IP address", device);

        // WHEN : execute the action you want to test

        LoginEvent saved = eventRepository.save(loginEvent);
        Optional<Event> readEvent = eventRepository.findById(saved.getEventId()); 

        // THEN : verify the result 

        // Mongo generated the _id and Spring injected it back
        assertThat(saved.getEventId()).isNotNull();
        assertThat(readEvent).isPresent();

        // _class "LOGIN" was resolved to the right subclass
        Event read = readEvent.get();
        assertThat(read).isInstanceOf(LoginEvent.class);

        if (read instanceof LoginEvent readLogin) {
            assertThat(readLogin.getEventType()).isEqualTo(EventType.LOGIN);
            assertThat(readLogin.getUserId()).isEqualTo(1);
            assertThat(readLogin.getTimestamp()).isEqualTo(now);
            assertThat(readLogin.getLoginStatus()).isEqualTo(LoginStatus.SUCCESS);
            assertThat(readLogin.getIpAddress()).isEqualTo("an IP address");
            // record equals compares the 3 components
            assertThat(readLogin.getDevice()).isEqualTo(device);
        }

    }

}
