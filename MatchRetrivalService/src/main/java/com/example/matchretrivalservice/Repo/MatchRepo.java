package com.example.matchretrivalservice.Repo;

import com.example.matchretrivalservice.Model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepo extends JpaRepository<Match, Long> {

    List<Match> findByUserId1OrUserId2(Long userId1, Long userId2);
}