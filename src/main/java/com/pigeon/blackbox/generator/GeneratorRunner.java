package com.pigeon.blackbox.generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.pigeon.blackbox.generator.population.UserGenerator;

// .\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=generate"

@Component
@Profile("generate")
public class GeneratorRunner implements CommandLineRunner { 
    
    private static final Logger log = LoggerFactory.getLogger(GeneratorRunner.class);

    private final UserGenerator userGenerator;

    public GeneratorRunner(UserGenerator userGenerator) {
        this.userGenerator = userGenerator; 
    }

    @Override
    public void run(String... args) {
        log.info("Event Generator launched successfully");

        int userCount = userGenerator.generateUsers();
        log.info("{} users generated", userCount); 
    }
}
