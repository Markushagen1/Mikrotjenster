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

    // Legger til brukernavn-feltet
    @Size(max = 50)
    private String username;

    // Oppdaterer konstruktøren for å inkludere brukernavn
    public UserProfile(String name, int age, double budget, String interests, String occupation, String username) {
        this.name = name;
        this.age = age;
        this.budget = budget;
        this.interests = interests;
        this.occupation = occupation;
        this.username = username;
    }
}



