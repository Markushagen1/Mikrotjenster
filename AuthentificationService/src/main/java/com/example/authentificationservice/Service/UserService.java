package com.example.authentificationservice.Service;

import com.example.authentificationservice.Repo.RoleRepo;
import com.example.authentificationservice.Repo.UserRepo;
import com.example.authentificationservice.Model.User;
import com.example.authentificationservice.Model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Henter bruker basert på brukernavn
    public Optional<User> findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    // Sjekker om brukernavnet allerede finnes
    public boolean existsByUsername(String username) {
        return userRepo.existsByUsername(username);
    }

    // Laster brukeren med brukernavn, inkludert roller
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Henter brukerens roller og konverterer til authorities
        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());

        // Returnerer en Spring Security User med brukernavn, passord og roller
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    // Lagrer en ny bruker og krypterer passordet
    public User saveUser(User user) {
        // Krypterer passordet
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Legger til rollen "ROLE_USER" som standard for nye brukere
        Set<Role> roles = new HashSet<>();

        // Sjekk om rollen "ROLE_USER" finnes, og legg den til
        Role userRole = roleRepo.findByName("ROLE_USER")
                .orElseGet(() -> {
                    // Hvis "ROLE_USER" ikke finnes, opprett den
                    Role newUserRole = new Role("ROLE_USER");
                    return roleRepo.save(newUserRole); // Lagre rollen i databasen
                });

        roles.add(userRole);
        user.setRoles(roles);

        // Lagrer brukeren i databasen
        return userRepo.save(user);
    }

}



