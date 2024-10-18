package com.example.matchingservice.DTO;

// Lag en DTO for LikeEvent
public class likeDTO {
    private Long likerId;
    private Long likedUserId;

    public likeDTO(Long likerId, Long likedUserId) {
        this.likerId = likerId;
        this.likedUserId = likedUserId;
    }

    public Long getLikerId() {
        return likerId;
    }

    public Long getLikedUserId() {
        return likedUserId;
    }
}


