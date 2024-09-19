package com.example.matchingservice.Controller;

import com.example.matchingservice.Model.Match;
import com.example.matchingservice.Repo.MatchRepo;
import com.example.matchingservice.Service.MatchingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/match")
public class MatchingController {


    private static final Logger logger = LoggerFactory.getLogger(MatchingController.class);

    @Autowired
    private MatchingService matchingService;

    @PostMapping("/register")
    public ResponseEntity<String> registerProfileForMatching(@RequestBody Map<String, Object> requestBody) {
        Long userId = Long.valueOf(requestBody.get("userId").toString());
        String preferences = requestBody.get("preferences").toString();

        logger.info("Received request to register user for matching: userId={}, preferences={}", userId, preferences);

        // Register the user for matching
        matchingService.registerUserForMatching(userId, preferences);

        logger.info("User registered for matching: userId={}", userId);

        return ResponseEntity.ok("User registered for matching!");
    }

    @PostMapping("/like")
    public ResponseEntity<String> likeProfile(@RequestBody Map<String, Object> requestBody) {
        Long likerId = Long.valueOf(requestBody.get("likerId").toString());
        Long likedUserId = Long.valueOf(requestBody.get("likedUserId").toString());

        logger.info("Received request to like profile: likerId={}, likedUserId={}", likerId, likedUserId);

        // Call service method to like the profile
        return matchingService.likeProfile(likerId, likedUserId);
    }

    // Corrected endpoint to get matches for a user
    @GetMapping("/matches/{userId}")
    public ResponseEntity<List<Long>> getMatches(@PathVariable Long userId) {
        logger.info("Received request to get matches for userId={}", userId);

        // Call service method to get matches
        List<Long> matchedUserIds = matchingService.getMatches(userId);

        logger.info("Matches for userId {}: {}", userId, matchedUserIds);
        return ResponseEntity.ok(matchedUserIds);
    }
}
