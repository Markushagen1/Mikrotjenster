package com.example.profilemanager.Controller;

import com.example.profilemanager.Model.UserProfile;
import com.example.profilemanager.Repo.UserProfileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/profiles")
public class TestWithRetrivalController {
    private final UserProfileRepo userProfileRepo;

    @Autowired
    public TestWithRetrivalController(UserProfileRepo userProfileRepo){
        this.userProfileRepo = userProfileRepo;
    }

    @PostMapping("/by-ids")
    public ResponseEntity<List<UserProfile>> getProfilesByIds(@RequestBody List<Long> userIds) {
        List<UserProfile> profiles = userProfileRepo.findByUserIdIn(userIds);
        return ResponseEntity.ok(profiles);
    }
}
