package com.example.matchingservice.Repo;
import com.example.matchingservice.Model.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface LikeRepo extends JpaRepository<Like, Long> {

    Optional<Like> findByLikerIdAndLikedUserId(Long likerId, Long likedUserId);
}

