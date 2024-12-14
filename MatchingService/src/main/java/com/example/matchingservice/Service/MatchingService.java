package com.example.matchingservice.Service;
import com.example.matchingservice.DTO.likeDTO;
import com.example.matchingservice.Model.Like;
import com.example.matchingservice.Model.Match;
import com.example.matchingservice.Repo.LikeRepo;
import com.example.matchingservice.Repo.MatchRepo;
import com.example.matchingservice.jwt.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Autowired
    private JwtUtil jwtUtil;

    public void registerUserForMatching(Long userId, String preferences) {
        logger.info("Registering user for matching: userId={}, preferences={}", userId, preferences);
        // Placeholder for actual implementation
    }

    public String likeProfile(Long likerId, Long likedUserId) {
        logger.info("Processing like: likerId={}, likedUserId={}", likerId, likedUserId);

        // Sjekk at brukeren ikke liker seg selv
        if (likerId.equals(likedUserId)) {
            logger.error("User cannot like themselves: likerId={}, likedUserId={}", likerId, likedUserId);
            throw new IllegalArgumentException("A user cannot like themselves.");
        }

        // Sjekk om like allerede eksisterer
        if (likeRepo.findByLikerIdAndLikedUserId(likerId, likedUserId).isPresent()) {
            logger.error("Like already exists: likerId={}, likedUserId={}", likerId, likedUserId);
            throw new IllegalArgumentException("Like already exists.");
        }

        // Lagre "like" i databasen
        likeRepo.save(new Like(likerId, likedUserId));
        logger.info("New like registered: likerId={}, likedUserId={}", likerId, likedUserId);

        // Publiser RabbitMQ-event for "like"
        rabbitTemplate.convertAndSend(exchangeName, likeRoutingKey, new likeDTO(likerId, likedUserId));

        // Sjekk for gjensidig like
        if (likeRepo.findByLikerIdAndLikedUserId(likedUserId, likerId).isPresent()) {
            // Lagre match
            matchRepo.save(new Match(likerId, likedUserId));
            logger.info("It's a match! likerId={}, likedUserId={}", likerId, likedUserId);

            // Publiser RabbitMQ-event for "match"
            rabbitTemplate.convertAndSend(exchangeName, matchRoutingKey, new likeDTO(likerId, likedUserId));

            return "It's a match!";
        }

        return "Like is registered.";
    }


    public List<Long> getMatches(Long userId) {
        // Hent matcher der brukeren er involvert som userId1 eller userId2
        List<Match> matches = matchRepo.findByUserId1OrUserId2(userId, userId);

        // Filtrer ut brukerens egen ID fra listen over matchedUserIds
        List<Long> matchedUserIds = matches.stream()
                .map(match -> userId.equals(match.getUserId1()) ? match.getUserId2() : match.getUserId1())
                .filter(matchedUserId -> !matchedUserId.equals(userId)) // Fjern brukeren selv
                .distinct()
                .toList();

        logger.info("Matches retrieved for userId {}: {}", userId, matchedUserIds);
        return matchedUserIds;
    }


    public Long extractUserIdFromToken(String token) {
        return jwtUtil.extractUserId(token);
    }
}






