package com.pigeon.blackbox.generator;

import java.util.Random;
import java.util.random.RandomGenerator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("generate")
public class GeneratorConfig {
    
    // seed : make the results reproducible on different launches/tests
    private static final long SEED = 42L;

    @Bean
    public RandomGenerator randomGenerator() {

        return new Random(SEED);

    }

}
