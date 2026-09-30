package com.pigeon.blackbox.generator.random;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.random.RandomGenerator;

public class WeightedPicker<T> {
    
    private final RandomGenerator randomGenerator; 

    private final List<T> elements; 

    private final double[] cumulativeWeights; 

    private final double totalWeight;

    public WeightedPicker(Map<T, Double> weightByElement, RandomGenerator randomGenerator) {
        
        this.randomGenerator = randomGenerator;
        
        this.elements = new ArrayList<>(); 
        
        if (weightByElement == null || weightByElement.isEmpty()) {
            throw new IllegalArgumentException("Map cannot be empty");
        }

        this.cumulativeWeights = new double[weightByElement.size()]; 

        double sum = 0; 
        int index = 0; 

        /* Add the cumulative weights to fill the array 
        and Fill the elements List */
        for (Map.Entry<T, Double> entry : weightByElement.entrySet()) {
            T key = entry.getKey(); 
            Double weight = entry.getValue();
            
            if (weight == null || weight < 0) {
                throw new IllegalArgumentException("Weight must be positive for element: " + key);
            }
            
            this.elements.add(key);

            sum += weight; 

            this.cumulativeWeights[index] = sum; 

            index++; 
        }

        if (sum == 0) {
            throw new IllegalArgumentException("Total weight cannot be equal to 0");
        }

        this.totalWeight = sum; 
    }

    public T pick() { 

        double roll = randomGenerator.nextDouble(this.totalWeight); 

        for (int i = 0; i < elements.size()  ; i++) { 
            if (cumulativeWeights[i] > roll) {
                return this.elements.get(i);
            }
        }

        return this.elements.get(elements.size() - 1);
    }
}
