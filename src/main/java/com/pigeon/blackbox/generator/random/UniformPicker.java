package com.pigeon.blackbox.generator.random;

import java.util.random.RandomGenerator;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component 
@Profile("generate")
public class UniformPicker {
    private final RandomGenerator randomGenerator;

    public UniformPicker(RandomGenerator randomGenerator) {
        this.randomGenerator = randomGenerator; 
    }

    public <E> E randomOf(E[] values) {
        return values[randomGenerator.nextInt(values.length)];
    }
}
