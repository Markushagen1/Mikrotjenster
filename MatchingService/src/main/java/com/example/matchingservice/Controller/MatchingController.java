package com.example.matchingservice.Controller;

import com.example.matchingservice.Service.MatchingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<?> likeProfile(@RequestBody Map<String, Object> likeRequest) {
        // Hent likerId og likedUserId fra request-body
        Object likerIdObj = likeRequest.get("likerId");
        Object likedUserIdObj = likeRequest.get("likedUserId");

        // Valider at begge verdiene finnes og er gyldige
        if (likerIdObj == null || likedUserIdObj == null) {
            logger.error("Invalid request: likerId or likedUserId is null");
            return ResponseEntity.badRequest().body("likerId and likedUserId are required");
        }

        try {
            Long likerId = Long.valueOf(likerIdObj.toString());
            Long likedUserId = Long.valueOf(likedUserIdObj.toString());

            // Kall service for å håndtere matching-logikken
            matchingService.likeProfile(likerId, likedUserId);
            return ResponseEntity.ok("Like registered successfully");
        } catch (NumberFormatException e) {
            logger.error("Invalid request: likerId or likedUserId is not a valid number", e);
            return ResponseEntity.badRequest().body("likerId and likedUserId must be valid numbers");
        } catch (Exception e) {
            logger.error("Error processing like request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request");
        }
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
