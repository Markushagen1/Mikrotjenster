package com.example.authentificationservice.Controller;

import com.example.authentificationservice.DTO.JwtResponse;
import com.example.authentificationservice.DTO.LoginRequest;
import com.example.authentificationservice.Model.User;
import com.example.authentificationservice.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        String jwt = authService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());
        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        System.out.println("Register endpoint called with: " + user.getUsername());
        User newUser = authService.registerUser(user);
        return ResponseEntity.ok(newUser);
    }
}

