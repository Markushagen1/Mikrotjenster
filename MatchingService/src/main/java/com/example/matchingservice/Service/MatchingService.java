package com.example.matchingservice.Service;
import com.example.matchingservice.DTO.likeDTO;
import com.example.matchingservice.Model.Like;
import com.example.matchingservice.Model.Match;
import com.example.matchingservice.Repo.LikeRepo;
import com.example.matchingservice.Repo.MatchRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MatchingService {

    private static final Logger logger = LoggerFactory.getLogger(MatchingService.class);

    private final LikeRepo likeRepo;
    private final MatchRepo matchRepo;
    private final RabbitTemplate rabbitTemplate;

    @Value("${amqp.exchange.name}")
    private String exchangeName;

    @Value("${amqp.routing.like.key}")
    private String likeRoutingKey;

    @Value("${amqp.routing.match.key}")
    private String matchRoutingKey;

    @Autowired
    public MatchingService(LikeRepo likeRepo, MatchRepo matchRepo, RabbitTemplate rabbitTemplate) {
        this.likeRepo = likeRepo;
        this.matchRepo = matchRepo;
        this.rabbitTemplate = rabbitTemplate;
    }

    // Registrerer bruker for matching (logikk kan tilpasses videre)
    public void registerUserForMatching(Long userId, String preferences) {
        logger.info("Registering user for matching in service layer: userId={}, preferences={}", userId, preferences);
        // Implement the registration logic here if necessary
        logger.info("User successfully registered in service layer: userId={}", userId);
    }

    public ResponseEntity<String> likeProfile(Long likerId, Long likedUserId) {
        // Sjekk om like allerede eksisterer
        Optional<Like> existingLike = likeRepo.findByLikerIdAndLikedUserId(likerId, likedUserId);
        if (existingLike.isPresent()) {
            logger.info("Like already exists: likerId={}, likedUserId={}", likerId, likedUserId);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Like already exists.");
        }

        // Lagre like i databasen
        likeRepo.save(new Like(likerId, likedUserId));
        logger.info("New like registered: likerId={}, likedUserId={}", likerId, likedUserId);

        // Publisere en melding til likeQueue
        likeDTO likeEvent = new likeDTO(likerId, likedUserId);
        rabbitTemplate.convertAndSend(exchangeName, likeRoutingKey, likeEvent);
        logger.info("Published 'profile.liked' event: likerId={}, likedUserId={}", likerId, likedUserId);


        // Sjekk etter gjensidig like (match)
        Optional<Like> reciprocalLike = likeRepo.findByLikerIdAndLikedUserId(likedUserId, likerId);
        if (reciprocalLike.isPresent()) {
            matchRepo.save(new Match(likerId, likedUserId));
            logger.info("It's a match! likerId={}, likedUserId={}", likerId, likedUserId);

            // Publisere en melding til matchQueue med LikeEventDTO
            rabbitTemplate.convertAndSend(exchangeName, matchRoutingKey, likeEvent);
            logger.info("Published 'profile.matched' event: likerId={}, likedUserId={}", likerId, likedUserId);

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





