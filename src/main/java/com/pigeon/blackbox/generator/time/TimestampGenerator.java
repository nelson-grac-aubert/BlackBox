package com.pigeon.blackbox.generator.time;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.random.RandomGenerator;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.pigeon.blackbox.generator.random.WeightedPicker;
import com.pigeon.blackbox.generator.SimulationPeriod;

@Component 
@Profile("generate")
public class TimestampGenerator {

    private static final ZoneId ZONE = ZoneId.of("Europe/Paris");

    // Time of day variation
    private static final double HIGH_ACTIVITY_WEIGHT = 1.0;
    private static final double LOW_ACTIVITY_WEIGHT = 0.2; 
    private static final int ACTIVITY_START_HOUR = 7; 
    private static final int ACTIVITY_END_HOUR = 23; 
    // Random variation 
    private static final double MIN_DAILY_VARIATION = 0.8;
    private static final double MAX_DAILY_VARIATION = 1.2;

    private final RandomGenerator randomGenerator;

    private final WeightedPicker<LocalDate> dayPicker;

    private final WeightedPicker<Integer> hourPicker;

    public TimestampGenerator(RandomGenerator randomGenerator) {
        this.randomGenerator = randomGenerator; 

        Map<DayOfWeek, Double> dayOfWeekWeights = dayOfWeekWeights();
        Map<LocalDate, Double> weightByDate = new LinkedHashMap<>();
        Map<Integer, Double> weightByHour = hourOfDayWeights();

        // TODO(minor) : events on jan 1st 0 to 1 are before SimulationPeriod.START because of timezone

        LocalDate firstDay = LocalDate.ofInstant(SimulationPeriod.START, ZONE);
        LocalDate endDay = LocalDate.ofInstant(SimulationPeriod.END, ZONE);

        for (LocalDate date = firstDay; date.isBefore(endDay); date = date.plusDays(1)) {
            double weight = dayOfWeekWeights.get(date.getDayOfWeek()) * randomGenerator.nextDouble(MIN_DAILY_VARIATION, MAX_DAILY_VARIATION);
            
            weightByDate.put(date, weight); 
        }

        this.dayPicker = new WeightedPicker<>(weightByDate, randomGenerator);
        this.hourPicker = new WeightedPicker<>(weightByHour, randomGenerator);

    }

    // Map days of week to their weights
    private static Map<DayOfWeek, Double> dayOfWeekWeights() { 
        Map<DayOfWeek, Double> weightsByDay = new EnumMap<>(DayOfWeek.class);

        // weekday full, saturday low, sunday lowest 
        weightsByDay.put(DayOfWeek.MONDAY, 1.0); 
        weightsByDay.put(DayOfWeek.TUESDAY, 1.0); 
        weightsByDay.put(DayOfWeek.WEDNESDAY, 1.0); 
        weightsByDay.put(DayOfWeek.THURSDAY, 1.0); 
        weightsByDay.put(DayOfWeek.FRIDAY, 1.0); 
        weightsByDay.put(DayOfWeek.SATURDAY, 0.4); 
        weightsByDay.put(DayOfWeek.SUNDAY, 0.2); 

        return weightsByDay; 
    }

    // Map hours of day to their weights
    private static Map<Integer, Double> hourOfDayWeights() {

        Map<Integer, Double> weightsByHour = new LinkedHashMap<>();

        for (int hour = 0; hour < 24; hour++) {
            double weight = (hour >= ACTIVITY_START_HOUR && hour < ACTIVITY_END_HOUR) ? HIGH_ACTIVITY_WEIGHT : LOW_ACTIVITY_WEIGHT;
            weightsByHour.put(hour, weight); 
        }   

        return weightsByHour;
    }
    

}
