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

        matchingService.registerUserForMatching(userId, preferences);

        return ResponseEntity.ok("User registered for matching!");
    }

    @PostMapping("/like")
    public ResponseEntity<String> likeProfile(@RequestBody Map<String, Object> likeRequest) {
        Long likerId = Long.valueOf(likeRequest.get("likerId").toString());
        Long likedUserId = Long.valueOf(likeRequest.get("likedUserId").toString());

        try {
            String result = matchingService.likeProfile(likerId, likedUserId);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid like request: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error processing like request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request.");
        }
    }

    @GetMapping("/matches/{userId}")
    public ResponseEntity<?> getMatches(@PathVariable Long userId) {
        List<Long> matchedUserIds = matchingService.getMatches(userId);

        if (matchedUserIds.isEmpty()) {
            return ResponseEntity.ok("No matches found.");
        }

        return ResponseEntity.ok(matchedUserIds);
    }
}

