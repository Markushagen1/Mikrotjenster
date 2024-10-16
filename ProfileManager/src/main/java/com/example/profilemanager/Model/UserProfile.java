package com.example.profilemanager.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    private Long userId;

    @NotBlank
    @Size(max = 50)
    private String name;

    @NotNull
    private int age;

    @NotNull
    private double budget;

    @Size(max = 255)
    private String interests;

    @Size(max = 50)
    private String occupation;

    public UserProfile(String name, int age, double budget, String intrests, String occupation) {
        this.name = name;
        this.age = age;
        this.budget = budget;
        this.interests = intrests;
        this.occupation = occupation;
    }
}


