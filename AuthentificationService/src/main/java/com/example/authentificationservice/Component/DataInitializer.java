package com.example.authentificationservice.Component;

import com.example.authentificationservice.Model.Role;
import com.example.authentificationservice.Repo.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepo roleRepo;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Initializing roles...");

        // Sjekk og opprett ROLE_USER
        if (roleRepo.findByName("ROLE_USER").isEmpty()) {
            Role userRole = new Role("ROLE_USER");
            roleRepo.save(userRole);
            System.out.println("ROLE_USER created");
        }

        // Sjekk og opprett ROLE_ADMIN (valgfritt, avhengig av applikasjonens behov)
        if (roleRepo.findByName("ROLE_ADMIN").isEmpty()) {
            Role adminRole = new Role("ROLE_ADMIN");
            roleRepo.save(adminRole);
            System.out.println("ROLE_ADMIN created");
        }
    }
}



