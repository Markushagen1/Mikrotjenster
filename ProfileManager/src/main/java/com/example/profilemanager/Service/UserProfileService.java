package com.example.profilemanager.Service;
import com.example.profilemanager.Exception.ResourceNotFoundException;
import com.example.profilemanager.Model.UserProfile;
import com.example.profilemanager.Model.profileEventPublisher;
import com.example.profilemanager.Repo.UserProfileRepo;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Transactional
public class UserProfileService {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileService.class);

    @Autowired
    private UserProfileRepo userProfileRepo;

    @Autowired
    private profileEventPublisher ProfileEventPublisher;

    // Lag en ny profil
    public UserProfile createProfile(UserProfile userProfile) {
        logger.info("Creating profile for user: {}", userProfile.getName());
        UserProfile savedProfile = userProfileRepo.save(userProfile);
        logger.info("Profile created successfully for user: {}", savedProfile.getName());

        // Publiser hendelse til RabbitMQ
        ProfileEventPublisher.publishProfileEvent(savedProfile);

        return savedProfile;
    }

    // Hent en profil
    public UserProfile getProfileById(Long id) {
        logger.info("Fetching profile for id: {}", id);
        return userProfileRepo.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Profile not found for id: {}", id);
                    return new ResourceNotFoundException("User not found with id: " + id);
                });
    }

    // Hent alle profiler
    public List<UserProfile> getAllProfiles() {
        logger.info("Fetching all profiles");
        List<UserProfile> profiles = userProfileRepo.findAll();
        logger.info("Total profiles found: {}", profiles.size());
        return profiles;
    }

    // Oppdater en profil
    public UserProfile updateProfile(Long id, UserProfile profileDetails) {
        logger.info("Updating profile for id: {}", id);
        UserProfile userProfile = userProfileRepo.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Profile not found for id: {}", id);
                    return new ResourceNotFoundException("User not found with id: " + id);
                });

        userProfile.setName(profileDetails.getName());
        userProfile.setAge(profileDetails.getAge());
        userProfile.setBudget(profileDetails.getBudget());
        userProfile.setInterests(profileDetails.getInterests());
        userProfile.setOccupation(profileDetails.getOccupation());

        UserProfile updatedProfile = userProfileRepo.save(userProfile);
        logger.info("Profile updated successfully for id: {}", id);

        // Publiser oppdatert hendelse til RabbitMQ
        ProfileEventPublisher.publishProfileEvent(updatedProfile);

        return updatedProfile;
    }

    // Slett en profil
    public void deleteProfile(Long id) {
        logger.info("Deleting profile for id: {}", id);
        UserProfile userProfile = userProfileRepo.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Profile not found for id: {}", id);
                    return new ResourceNotFoundException("User not found with id: " + id);
                });

        userProfileRepo.delete(userProfile);
        logger.info("Profile deleted successfully for id: {}", id);
    }

    public UserProfile getProfileByUsername(String username) {
        return userProfileRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("No profile found for username: " + username));
    }
}


