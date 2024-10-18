package com.example.matchingservice.configurations;
import com.example.matchingservice.DTO.likeDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ProfileEventListener {

    @RabbitListener(queues = "${amqp.queue.match.name}")
    public void handleProfileMatched(likeDTO likeEvent) {
        Long likerId = likeEvent.getLikerId();
        Long likedUserId = likeEvent.getLikedUserId();
        System.out.println("Received match event: likerId=" + likerId + ", likedUserId=" + likedUserId);
        // Legg til logikk for å håndtere matcher
    }


    @RabbitListener(queues = "${amqp.queue.like.name}")
    public void handleProfileLiked(likeDTO likeEvent) {
        Long likerId = likeEvent.getLikerId();
        Long likedUserId = likeEvent.getLikedUserId();
        System.out.println("Received like event: likerId=" + likerId + ", likedUserId=" + likedUserId);
        // Legg til logikk for å håndtere likes
    }
}



