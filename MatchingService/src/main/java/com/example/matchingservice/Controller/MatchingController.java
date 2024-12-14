package com.example.matchingservice.Controller;

import com.example.matchingservice.Service.MatchingService;
import com.example.matchingservice.jwt.JwtUtil;
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

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<String> registerProfileForMatching(@RequestBody Map<String, Object> requestBody) {
        if (!requestBody.containsKey("userId") || !requestBody.containsKey("preferences")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing userId or preferences.");
        }

        Long userId;
        String preferences;

        try {
            userId = Long.valueOf(requestBody.get("userId").toString());
            preferences = requestBody.get("preferences").toString();
        } catch (Exception e) {
            logger.error("Error parsing request body for /register", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data format.");
        }

        logger.info("Received request to register user for matching: userId={}, preferences={}", userId, preferences);

        try {
            matchingService.registerUserForMatching(userId, preferences);
            return ResponseEntity.ok("User registered for matching!");
        } catch (Exception e) {
            logger.error("Error in registerUserForMatching", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while registering user for matching.");
        }
    }

    @PostMapping("/like")
    public ResponseEntity<String> likeProfile(
            @RequestBody Map<String, Object> likeRequest,
            @RequestHeader(value = "Authorization", required = false) String token // Gjør header valgfri
    ) {
        Long likerId;

        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            likerId = jwtUtil.extractUserId(jwtToken); // Hent likerId fra tokenet
        } else {
            if (!likeRequest.containsKey("likerId")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing likerId.");
            }
            likerId = Long.valueOf(likeRequest.get("likerId").toString()); // Fallback for Postman
        }

        if (!likeRequest.containsKey("likedUserId")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing likedUserId.");
        }

        Long likedUserId = Long.valueOf(likeRequest.get("likedUserId").toString());

        try {
            String result = matchingService.likeProfile(likerId, likedUserId);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid like request: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }



    @GetMapping("/matches/{userId}")
    public ResponseEntity<?> getMatches(@PathVariable Long userId) {
        if (userId == null || userId <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid userId.");
        }

        try {
            List<Long> matchedUserIds = matchingService.getMatches(userId);
            return ResponseEntity.ok(matchedUserIds);
        } catch (Exception e) {
            logger.error("Error retrieving matches for userId {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while retrieving matches.");
        }
    }
}


