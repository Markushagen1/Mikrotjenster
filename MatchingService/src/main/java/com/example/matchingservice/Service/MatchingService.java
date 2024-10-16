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

    // Registrerer bruker for matching (logikk kan tilpasses videre)
    public void registerUserForMatching(Long userId, String preferences) {
        logger.info("Registering user for matching in service layer: userId={}, preferences={}", userId, preferences);
        // Implement the registration logic here if necessary
        logger.info("User successfully registered in service layer: userId={}", userId);
    }

    // Metode for å like en profil
    public ResponseEntity<String> likeProfile(Long likerId, Long likedUserId) {
        // Sjekker om like allerede eksisterer
        Optional<Like> existingLike = likeRepo.findByLikerIdAndLikedUserId(likerId, likedUserId);
        if (existingLike.isPresent()) {
            logger.info("Like already exists: likerId={}, likedUserId={}", likerId, likedUserId);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Like already exists.");
        }

        // Lagrer ny like i databasen
        likeRepo.save(new Like(likerId, likedUserId));
        logger.info("New like registered: likerId={}, likedUserId={}", likerId, likedUserId);

        // Sjekker om det finnes en gjensidig like (match)
        Optional<Like> reciprocalLike = likeRepo.findByLikerIdAndLikedUserId(likedUserId, likerId);
        if (reciprocalLike.isPresent()) {
            // Oppretter en ny match hvis det er en gjensidig like
            matchRepo.save(new Match(likerId, likedUserId));
            logger.info("It's a match! likerId={}, likedUserId={}", likerId, likedUserId);
            return ResponseEntity.ok("It's a match!");
        }

        return ResponseEntity.ok("Like is registered.");
    }

    // Henter alle matches for en bruker
    public List<Long> getMatches(Long userId) {
        List<Match> matches = matchRepo.findByUserId1OrUserId2(userId, userId);
        List<Long> matchedUserIds = new ArrayList<>();

        // Går gjennom alle matches og finner tilknyttede bruker-IDer
        for (Match match : matches) {
            if (userId.equals(match.getUserId1())) {
                matchedUserIds.add(match.getUserId2());
            } else if (userId.equals(match.getUserId2())) {
                matchedUserIds.add(match.getUserId1());
            }
        }

        logger.info("Matches retrieved for userId {}: {}", userId, matchedUserIds);
        return matchedUserIds;
    }
}


