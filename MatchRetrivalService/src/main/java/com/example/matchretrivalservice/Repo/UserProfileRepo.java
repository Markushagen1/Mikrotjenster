package com.example.matchretrivalservice.Repo;

import com.example.matchretrivalservice.Model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserProfileRepo extends JpaRepository<UserProfile, Long> {
    List<UserProfile> findByUserIdIn(List<Long> userIds);
}


