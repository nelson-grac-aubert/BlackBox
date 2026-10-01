package com.pigeon.blackbox.domain.model;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users") 
@TypeAlias("USER")
public class User {
    @Id
    private Integer userId; 

    private String lastName;

    private String firstName;

    private Instant signupDate; 

    public User(Integer userId, String lastName, String firstName, Instant signupDate) {
        this.userId = userId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.signupDate = signupDate;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getLastName() { 
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public Instant getSignupDate() {
        return signupDate;
    }
}   
