package com.pigeon.blackbox.generator.population;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.random.RandomGenerator;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.pigeon.blackbox.domain.repository.UserRepository;
import com.pigeon.blackbox.domain.model.User;
import com.pigeon.blackbox.generator.SimulationPeriod;
import com.pigeon.blackbox.generator.random.WeightedPicker;

@Component
@Profile("generate")
public class UserGenerator {
    private static final int TOTAL_USERS = 2000;
    private static final int BIG_USERS = 400; 
    private static final double BIG_USER_WEIGHT = 16.0; 
    private static final double SMALL_USER_WEIGHT = 1.0;  
    private static final String[] FIRST_NAMES = {"John", "Jane", "Nelson", "Rayane", "Hugo", "Nicolas", "Amel", "Inès", "Florian", "Haïk", "Morgan", "Hello"};
    private static final String[] LAST_NAMES = {"Doe", "Don't", "Grac", "Belaloui", "Picard", "Dupont", "Duchêne", "Martin", "Coucou", "Salut", "World"};

    private final UserRepository userRepository;

    private final RandomGenerator randomGenerator; 

    public UserGenerator(UserRepository userRepository, RandomGenerator randomGenerator) {
        this.userRepository = userRepository;
        this.randomGenerator = randomGenerator; 
    }

    public int generateUsers() {

        userRepository.deleteAll();

        Instant userSignupEndDate = SimulationPeriod.START; 
        Instant userSignupStartDate = userSignupEndDate.minus(730, ChronoUnit.DAYS);

        List<User> users = new ArrayList<>(); 

        for (int id = 1; id <= TOTAL_USERS; id++) {
            String firstName = FIRST_NAMES[randomGenerator.nextInt(FIRST_NAMES.length)];
            String lastName = LAST_NAMES[randomGenerator.nextInt(LAST_NAMES.length)];

            long randomSignupMillis = randomGenerator.nextLong(userSignupStartDate.toEpochMilli(), userSignupEndDate.toEpochMilli());
            Instant signupDate = Instant.ofEpochMilli(randomSignupMillis);

            users.add(new User(id, lastName, firstName, signupDate));

        }

        userRepository.saveAll(users); 

        return users.size(); 

    }

    public WeightedPicker<Integer> createUserPicker() { 
        Map<Integer, Double> weightById = new LinkedHashMap<>();

        for (int id = 1; id <= TOTAL_USERS; id++) {
            double weight = (id <= BIG_USERS ? BIG_USER_WEIGHT : SMALL_USER_WEIGHT);
            weightById.put(id, weight);  
        }

        return new WeightedPicker<>(weightById, randomGenerator);

    }
}
