package com.example.profilemanager.Controller;
import com.example.profilemanager.Model.UserProfile;
import com.example.profilemanager.Service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    @Autowired
    private UserProfileService userProfileService;

    // Create a new profile
    @PostMapping
    public UserProfile createProfile(@RequestBody UserProfile userProfile) {
        return userProfileService.createProfile(userProfile);
    }

    // Fetch an existing profile by ID
    @GetMapping("/{id}")
    public UserProfile getProfile(@PathVariable Long id) {
        return userProfileService.getProfileById(id);
    }

    // Update an existing profile
    @PutMapping("/{id}")
    public ResponseEntity<UserProfile> updateProfile(@PathVariable Long id, @RequestBody UserProfile profileDetails) {
        UserProfile updatedUserProfile = userProfileService.updateProfile(id, profileDetails);
        return ResponseEntity.ok(updatedUserProfile);
    }

    // Delete a profile
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProfile(@PathVariable Long id) {
        userProfileService.deleteProfile(id);
        return ResponseEntity.ok().build();
    }
}
