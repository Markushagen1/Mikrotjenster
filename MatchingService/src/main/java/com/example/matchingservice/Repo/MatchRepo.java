package com.example.matchingservice.Repo;
import com.example.matchingservice.Model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MatchRepo extends JpaRepository<Match, Long> {

    List<Match> findByUserId1OrUserId2(Long userId1, Long userId2);
}
