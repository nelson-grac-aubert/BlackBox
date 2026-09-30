package com.pigeon.blackbox.generator.random;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.random.RandomGenerator;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class WeightedPickerTest {
    @Test 
    void picksElementsProportionallyToTheirWeights() { 
        // GIVEN
        RandomGenerator randomGenerator = new Random(42); 

        Map<String, Double> elementsAndTheirWeights = Map.of("A", 5.0, "B", 3.0, "C", 2.0);
        
        WeightedPicker<String> picker = new WeightedPicker<>(elementsAndTheirWeights, randomGenerator);

        Map<String, Integer> counter = new HashMap<>();

        // WHEN 

        for (int i = 0; i < 10000; i++) { 
            counter.merge(picker.pick(), 1, Integer::sum); 
        }
        
        // THEN 

        assertThat(counter.get("A")).isCloseTo(5000, within(300));
        assertThat(counter.get("B")).isCloseTo(3000, within(300));
        assertThat(counter.get("C")).isCloseTo(2000, within(300));  
    }

    // TODO : add test with weight = 0
    // TODO : add test with empty map
    // TODO : add test with negative weight 
}
