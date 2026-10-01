package com.pigeon.blackbox.generator;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.pigeon.blackbox.domain.enums.EventType;
import com.pigeon.blackbox.domain.model.Event;
import com.pigeon.blackbox.domain.repository.EventRepository;
import com.pigeon.blackbox.generator.factory.EventFactory;
import com.pigeon.blackbox.generator.random.WeightedPicker;
import com.pigeon.blackbox.generator.time.TimestampGenerator;

@Component 
@Profile("generate")
public class EventGenerator {
    private final EventRepository eventRepository; 
    
    private final TimestampGenerator timestampGenerator;

    private final Map<EventType, EventFactory> factoryByType;

    private final Map<EventType, Integer> volumeByType;

    public EventGenerator(
        EventRepository eventRepository, 
        TimestampGenerator timestampGenerator,
        List<EventFactory> factories) {

        this.eventRepository = eventRepository;
        this.timestampGenerator = timestampGenerator;
        this.factoryByType = new EnumMap<>(EventType.class);
        this.volumeByType = eventVolumes();
        
        // For each event factory, assign a produced event type
        for (EventFactory factory : factories) {
            factoryByType.put(factory.type(), factory);
        }

    }

    // Realistic distribution of 100k event types over a year
    private static Map<EventType, Integer> eventVolumes() {
        Map<EventType, Integer> volumes = new EnumMap<>(EventType.class);
        volumes.put(EventType.API_CALL, 50000);
        volumes.put(EventType.LOGIN, 20000);
        volumes.put(EventType.NOTIFICATION, 20000);
        volumes.put(EventType.ERROR, 8000);
        volumes.put(EventType.PAYMENT, 4000);
        return volumes;
    }

    public int generateEvents(WeightedPicker<Integer> userPicker) {
        
        eventRepository.deleteAll();

        List<Event> events = new ArrayList<>();

        // get the number of each event type to generate
        for (Map.Entry<EventType, Integer> entry : volumeByType.entrySet()) {
            EventFactory factory = factoryByType.get(entry.getKey());
            int volume = entry.getValue();

            // weighted Pareto user distribution
            for (int i = 0; i < volume; i++) {
                events.add(factory.create(userPicker.pick(), timestampGenerator.randomTimestamp()));
            }
        }

        eventRepository.saveAll(events);
        return events.size();

    }
}
