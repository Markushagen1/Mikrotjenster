package com.example.matchingservice.Service;
import com.example.matchingservice.Model.Like;
import com.example.matchingservice.Model.Match;
import com.example.matchingservice.Repo.LikeRepo;
import com.example.matchingservice.Repo.MatchRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MatchingService {

    private static final Logger logger = LoggerFactory.getLogger(MatchingService.class);

    private final LikeRepo likeRepo;
    private final MatchRepo matchRepo;

    @Autowired
    public MatchingService(LikeRepo likeRepo, MatchRepo matchRepo) {
        this.likeRepo = likeRepo;
        this.matchRepo = matchRepo;
    }

    // Method to register user for matching
    public void registerUserForMatching(Long userId, String preferences) {
        logger.info("Registering user for matching in service layer: userId={}, preferences={}", userId, preferences);
        // Implement the registration logic here
        logger.info("User successfully registered in service layer: userId={}", userId);
    }

    // Method to like a profile
    public ResponseEntity<String> likeProfile(Long likerId, Long likedUserId) {
        // Check if the like already exists
        Optional<Like> existingLike = likeRepo.findByLikerIdAndLikedUserId(likerId, likedUserId);
        if (existingLike.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Like already exists.");
        }

        // Save the new like
        likeRepo.save(new Like(likerId, likedUserId));
        logger.info("New like registered: likerId={}, likedUserId={}", likerId, likedUserId);

        // Check if there is a mutual like between users
        Optional<Like> reciprocalLike = likeRepo.findByLikerIdAndLikedUserId(likedUserId, likerId);
        if (reciprocalLike.isPresent()) {
            // Create a new match
            matchRepo.save(new Match(likerId, likedUserId));
            logger.info("It's a match! likerId={}, likedUserId={}", likerId, likedUserId);
            return ResponseEntity.ok("It's a match!");
        }

        return ResponseEntity.ok("Like is registered.");
    }

    // Get matches for a user
    public List<Long> getMatches(Long userId) {
        List<Match> matches = matchRepo.findByUserId1OrUserId2(userId, userId);
        List<Long> matchedUserIds = new ArrayList<>();

        for (Match match : matches) {
            if (userId.equals(match.getUserId1())) {
                matchedUserIds.add(match.getUserId2());
            } else if (userId.equals(match.getUserId2())) {
                matchedUserIds.add(match.getUserId1());
            }
        }
        return matchedUserIds;
    }
}

