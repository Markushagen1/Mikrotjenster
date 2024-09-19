package com.example.profilemanager.Repo;

import com.example.profilemanager.Model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepo extends JpaRepository<UserProfile, Long> {
}
