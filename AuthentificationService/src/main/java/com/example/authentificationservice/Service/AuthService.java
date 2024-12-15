package com.example.authentificationservice.Service;
import com.example.authentificationservice.Component.JwtUtil;
import com.example.authentificationservice.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    // Registrer en bruker (bare brukernavn/passord)
    public User registerUser(User user) {
        if (userService.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Error: Username is already taken!");
        }
        // Lagre kun brukernavn og passord i databasen
        return userService.saveUser(user);
    }

    // Autentiser bruker og generer JWT-token
    public String authenticateUser(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Hent brukeren fra databasen for å få tilgang til userId
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Error: User not found"));

        // Generer JWT-token med både brukernavn og userId
        return jwtUtil.generateToken(username, user.getId());
    }
}









