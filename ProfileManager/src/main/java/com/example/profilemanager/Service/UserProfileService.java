package com.example.profilemanager.Service;
import com.example.profilemanager.Exception.ResourceNotFoundException;
import com.example.profilemanager.Model.UserProfile;
import com.example.profilemanager.Repo.UserProfileRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class UserProfileService {

    @Autowired
    private UserProfileRepo userProfileRepo;

    // Lag en ny profil
    public UserProfile createProfile(UserProfile userProfile) {
        return userProfileRepo.save(userProfile);
    }

    // Hent en profil
    public UserProfile getProfileById(Long id) {
        return userProfileRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    // Hent alle profiler
    public List<UserProfile> getAllProfiles() {
        return userProfileRepo.findAll();
    }

    // Oppdater en profil
    public UserProfile updateProfile(Long id, UserProfile profileDetails) {
        UserProfile userProfile = userProfileRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        userProfile.setName(profileDetails.getName());
        userProfile.setAge(profileDetails.getAge());
        userProfile.setBudget(profileDetails.getBudget());
        userProfile.setInterests(profileDetails.getInterests());
        userProfile.setOccupation(profileDetails.getOccupation());

        return userProfileRepo.save(userProfile);
    }

    // Slett en profil
    public void deleteProfile(Long id) {
        UserProfile userProfile = userProfileRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        userProfileRepo.delete(userProfile);
    }
}
