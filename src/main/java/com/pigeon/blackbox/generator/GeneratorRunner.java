package com.pigeon.blackbox.generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.pigeon.blackbox.domain.model.Event;
import com.pigeon.blackbox.generator.population.UserGenerator;
import com.pigeon.blackbox.generator.random.WeightedPicker;

// .\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=generate"

@Component
@Profile("generate")
public class GeneratorRunner implements CommandLineRunner { 
    
    private static final Logger log = LoggerFactory.getLogger(GeneratorRunner.class);

    private final UserGenerator userGenerator;

    private final EventGenerator eventGenerator;

    public GeneratorRunner(UserGenerator userGenerator, EventGenerator eventGenerator) {
        this.userGenerator = userGenerator; 
        this.eventGenerator = eventGenerator; 
    }

    @Override
    public void run(String... args) {
        log.info("Event Generator launched successfully");
        
        // generate Users
        int userCount = userGenerator.generateUsers();
        log.info("{} users generated", userCount); 

        // generate Events
        WeightedPicker<Integer> userPicker = userGenerator.createUserPicker();
        int eventCount = eventGenerator.generateEvents(userPicker);
        log.info("{} events generated", eventCount);

    }
}
