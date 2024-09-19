package com.example.matchingservice.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_like")  // Renaming so no conflict with "Like", some sql crap
@Getter
@Setter
@NoArgsConstructor
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long likerId;
    private Long likedUserId;

    public Like(Long likerId, Long likedUserId) {
        this.likerId = likerId;
        this.likedUserId = likedUserId;
    }
}
