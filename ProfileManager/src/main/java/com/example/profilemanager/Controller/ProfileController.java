package com.example.profilemanager.Controller;
import com.example.profilemanager.Model.UserProfile;
import com.example.profilemanager.Service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    @Autowired
    private UserProfileService userProfileService;

    // Opprett en ny profil (POST)
    @PostMapping
    public UserProfile createProfile(@RequestBody UserProfile userProfile) {
        return userProfileService.createProfile(userProfile);
    }

    // Hent en eksisterende profil (GET)
    @GetMapping("/{id}")
    public UserProfile getProfile(@PathVariable Long id) {
        return userProfileService.getProfileById(id);
    }

    // Hent alle profiler (GET)
    @GetMapping
    public List<UserProfile> getAllProfiles() {
        return userProfileService.getAllProfiles();
    }

    // Oppdater en eksisterende profil (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<UserProfile> updateProfile(@PathVariable Long id, @RequestBody UserProfile profileDetails) {
        UserProfile updatedUserProfile = userProfileService.updateProfile(id, profileDetails);
        return ResponseEntity.ok(updatedUserProfile);
    }

    // Slett en profil (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProfile(@PathVariable Long id) {
        userProfileService.deleteProfile(id);
        return ResponseEntity.ok().build();
    }
}

