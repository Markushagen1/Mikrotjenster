package com.example.authentificationservice.Service;
import com.example.authentificationservice.Component.JwtUtil;
import com.example.authentificationservice.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    public String authenticateUser(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generer JWT-token
        UserDetails userDetails = userService.loadUserByUsername(username);
        return jwtUtil.generateToken(userDetails);
    }

    public User registerUser(User user) {
        if (userService.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Error: Username is already taken!");
        }
        return userService.saveUser(user);
    }
}



