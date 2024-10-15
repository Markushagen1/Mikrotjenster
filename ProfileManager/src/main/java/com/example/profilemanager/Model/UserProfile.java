package com.example.profilemanager.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;
    private String name;
    private int age;
    private double budget;
    private String interests;
    private String occupation;

    public UserProfile(String name, int age, double budget, String intrests, String occupation) {
        this.name = name;
        this.age = age;
        this.budget = budget;
        this.interests = intrests;
        this.occupation = occupation;
    }
}


