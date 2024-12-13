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

    public void registerUserForMatching(Long userId, String preferences) {
        logger.info("Registering user for matching: userId={}, preferences={}", userId, preferences);
        // Implementation for future use
    }

    public String likeProfile(Long likerId, Long likedUserId) {
        // Sjekk at brukeren ikke prøver å like seg selv
        if (likerId.equals(likedUserId)) {
            throw new IllegalArgumentException("A user cannot like themselves.");
        }

        // Sjekk om like allerede eksisterer
        if (likeRepo.findByLikerIdAndLikedUserId(likerId, likedUserId).isPresent()) {
            throw new IllegalArgumentException("Like already exists.");
        }

        likeRepo.save(new Like(likerId, likedUserId));
        logger.info("New like registered: likerId={}, likedUserId={}", likerId, likedUserId);

        try {
            rabbitTemplate.convertAndSend(exchangeName, likeRoutingKey, new likeDTO(likerId, likedUserId));
            logger.info("Published 'profile.liked' event: likerId={}, likedUserId={}", likerId, likedUserId);
        } catch (Exception e) {
            logger.error("Failed to publish RabbitMQ message for like", e);
        }

        if (likeRepo.findByLikerIdAndLikedUserId(likedUserId, likerId).isPresent()) {
            matchRepo.save(new Match(likerId, likedUserId));
            logger.info("It's a match! likerId={}, likedUserId={}", likerId, likedUserId);

            try {
                rabbitTemplate.convertAndSend(exchangeName, matchRoutingKey, new likeDTO(likerId, likedUserId));
                logger.info("Published 'profile.matched' event: likerId={}, likedUserId={}", likerId, likedUserId);
            } catch (Exception e) {
                logger.error("Failed to publish RabbitMQ message for match", e);
            }

            return "It's a match!";
        }

        return "Like is registered.";
    }


    public List<Long> getMatches(Long userId) {
        List<Match> matches = matchRepo.findByUserId1OrUserId2(userId, userId);
        List<Long> matchedUserIds = matches.stream()
                .map(match -> userId.equals(match.getUserId1()) ? match.getUserId2() : match.getUserId1())
                .distinct()
                .toList();

        logger.info("Matches retrieved for userId {}: {}", userId, matchedUserIds);
        return matchedUserIds;
    }

}




