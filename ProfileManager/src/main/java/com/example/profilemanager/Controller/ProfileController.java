package com.example.profilemanager.Controller;
import com.example.profilemanager.Jwt.JwtUtil;
import com.example.profilemanager.Model.UserProfile;
import com.example.profilemanager.Service.UserProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private JwtUtil jwtUtil;

    // Opprett en ny profil (bruker må være logget inn)
    @PostMapping
    public ResponseEntity<UserProfile> createProfile(@Valid @RequestBody UserProfile userProfile, @RequestHeader("Authorization") String token) {
        // Fjern "Bearer " fra starten av tokenet
        String jwtToken = token.substring(7);

        // Hent brukernavn fra JWT-token
        String username = jwtUtil.extractUsername(jwtToken);

        // Sett brukernavn på profilen
        userProfile.setUsername(username);

        // Opprett profilen
        UserProfile createdProfile = userProfileService.createProfile(userProfile);

        return ResponseEntity.ok(createdProfile);
    }

    // Hent en eksisterende profil (GET)
    @GetMapping("/{id}")
    public ResponseEntity<UserProfile> getProfile(@PathVariable Long id) {
        logger.info("Fetching profile with id: {}", id);
        UserProfile userProfile = userProfileService.getProfileById(id);
        logger.info("Profile fetched for id: {}", id);
        return ResponseEntity.ok(userProfile);
    }

    // Hent alle profiler (GET)
    @GetMapping
    public ResponseEntity<List<UserProfile>> getAllProfiles() {
        logger.info("Fetching all profiles");
        List<UserProfile> profiles = userProfileService.getAllProfiles();
        logger.info("Total profiles fetched: {}", profiles.size());
        return ResponseEntity.ok(profiles);
    }

    // Oppdater en eksisterende profil (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<UserProfile> updateProfile(@PathVariable Long id, @Valid @RequestBody UserProfile profileDetails) {
        logger.info("Request received to update profile for id: {}", id);
        UserProfile updatedProfile = userProfileService.updateProfile(id, profileDetails);
        logger.info("Profile updated successfully for id: {}", id);
        return ResponseEntity.ok(updatedProfile);
    }

    // Slett en profil (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProfile(@PathVariable Long id) {
        logger.info("Request received to delete profile with id: {}", id);
        userProfileService.deleteProfile(id);
        logger.info("Profile deleted successfully for id: {}", id);
        return ResponseEntity.ok().build();
    }
}




