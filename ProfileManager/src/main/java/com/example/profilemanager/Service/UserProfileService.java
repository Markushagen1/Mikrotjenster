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
import java.util.Map;


@Service
public class UserProfileService {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileService.class);

    @Autowired
    private UserProfileRepo userProfileRepo;

    @Autowired
    private RestTemplate restTemplate;

    // Create a new profile
    public UserProfile createProfile(UserProfile userProfile) {
        // Save the new profile
        UserProfile savedProfile = userProfileRepo.save(userProfile);
        logger.info("Profile created: {}", savedProfile);

        // Prepare the request for MatchingService
        String matchingServiceUrl = "http://localhost:8081/api/match/register"; // Adjust URL as needed
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("userId", savedProfile.getUserId());
        requestBody.put("preferences", "some-preferences"); // Example data

        // Logs before calling the MatchingService
        logger.info("Calling MatchingService to register user for matching: userId={}, preferences={}", savedProfile.getUserId(), "some-preferences");

        // Make a POST request to MatchingService
        try {
            restTemplate.postForObject(matchingServiceUrl, requestBody, String.class);
            logger.info("Successfully registered user with MatchingService.");
        } catch (Exception e) {
            logger.error("Failed to register user with MatchingService: {}", e.getMessage());
        }

        return savedProfile;
    }

    // Fetch an existing profile by ID
    public UserProfile getProfileById(Long id) {
        logger.info("Fetching profile with id: {}", id);
        return userProfileRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    // Update an existing profile
    public UserProfile updateProfile(Long id, UserProfile profileDetails) {
        UserProfile userProfile = userProfileRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // Update the profile details
        userProfile.setName(profileDetails.getName());
        userProfile.setAge(profileDetails.getAge());
        userProfile.setBudget(profileDetails.getBudget());
        userProfile.setIntrests(profileDetails.getIntrests());
        userProfile.setOccupation(profileDetails.getOccupation());

        UserProfile updatedUserProfile = userProfileRepo.save(userProfile);
        logger.info("Profile updated: {}", updatedUserProfile);

        return updatedUserProfile;
    }

    // Delete a profile
    public void deleteProfile(Long id) {
        UserProfile userProfile = userProfileRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        userProfileRepo.delete(userProfile);
        logger.info("Profile deleted with id: {}", id);
    }
}