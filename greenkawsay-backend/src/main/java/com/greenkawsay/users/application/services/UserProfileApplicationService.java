package com.greenkawsay.users.application.services;

import com.greenkawsay.shared.domain.valueobjects.Email;
import com.greenkawsay.users.application.ports.in.UserProfileServicePort;
import com.greenkawsay.users.domain.exceptions.UserProfileNotFoundException;
import com.greenkawsay.users.domain.models.UserProfile;
import com.greenkawsay.users.domain.repositories.UserProfileRepository;
import com.greenkawsay.users.domain.valueobjects.ImpactScore;
import com.greenkawsay.users.domain.valueobjects.Role;
import com.greenkawsay.users.domain.valueobjects.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Application service for UserProfile operations
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserProfileApplicationService implements UserProfileServicePort {

    private final UserProfileRepository userProfileRepository;

    @Override
    public UserProfile createUserProfile(UserProfile userProfile) {
        log.info("Creating user profile with ID: {}", userProfile.getId().getValue());
        
        UserProfile savedProfile = userProfileRepository.save(userProfile);
        log.info("User profile created successfully with ID: {}", savedProfile.getId().getValue());
        
        return savedProfile;
    }

    @Override
    public UserProfile updateUserProfile(UserId userId, UserProfile updatedProfile) {
        log.info("Updating user profile with ID: {}", userId.getValue());
        
        UserProfile existingProfile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new UserProfileNotFoundException(userId));
        
        // Use domain methods to update the profile
        UserProfile profileToUpdate = existingProfile.updateProfile(
            updatedProfile.getFirstName(), 
            updatedProfile.getLastName(), 
            updatedProfile.getAvatarUrl()
        );
        
        UserProfile savedProfile = userProfileRepository.save(profileToUpdate);
        log.info("User profile updated successfully with ID: {}", savedProfile.getId().getValue());
        
        return savedProfile;
    }

    @Override
    public Optional<UserProfile> getUserProfileById(UserId userId) {
        log.debug("Getting user profile by ID: {}", userId.getValue());
        return userProfileRepository.findById(userId);
    }

    @Override
    public Optional<UserProfile> getUserProfileByEmail(String email) {
        log.debug("Getting user profile by email: {}", email);
        List<UserProfile> allProfiles = userProfileRepository.findAll();
        return allProfiles.stream()
                .filter(profile -> profile.getEmail().getValue().equalsIgnoreCase(email))
                .findFirst();
    }

    @Override
    public Optional<UserProfile> getUserProfileByKeycloakId(String keycloakId) {
        log.debug("Getting user profile by Keycloak ID: {}", keycloakId);
        List<UserProfile> allProfiles = userProfileRepository.findAll();
        return allProfiles.stream()
                .filter(profile -> profile.getKeycloakId().equalsIgnoreCase(keycloakId))
                .findFirst();
    }

    @Override
    public Page<UserProfile> getAllUserProfiles(Pageable pageable) {
        log.debug("Getting all user profiles with pagination");
        List<UserProfile> allProfiles = userProfileRepository.findAll();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), allProfiles.size());
        List<UserProfile> pageContent = allProfiles.subList(start, end);
        return new PageImpl<>(pageContent, pageable, allProfiles.size());
    }

    @Override
    public Page<UserProfile> getUserProfilesByRole(String role, Pageable pageable) {
        log.debug("Getting user profiles by role: {}", role);
        List<UserProfile> allProfiles = userProfileRepository.findAll();
        List<UserProfile> matchingProfiles = allProfiles.stream()
                .filter(profile -> profile.getRole().getValueAsString().equalsIgnoreCase(role))
                .toList();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), matchingProfiles.size());
        List<UserProfile> pageContent = matchingProfiles.subList(start, end);
        return new PageImpl<>(pageContent, pageable, matchingProfiles.size());
    }

    @Override
    public void deactivateUserProfile(UserId userId) {
        log.info("Deactivating user profile with ID: {}", userId.getValue());
        
        UserProfile existingProfile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new UserProfileNotFoundException(userId));
        
        UserProfile deactivatedProfile = existingProfile.deactivate();
        userProfileRepository.save(deactivatedProfile);
        log.info("User profile deactivated successfully with ID: {}", userId.getValue());
    }

    @Override
    public void activateUserProfile(UserId userId) {
        log.info("Activating user profile with ID: {}", userId.getValue());
        
        UserProfile existingProfile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new UserProfileNotFoundException(userId));
        
        UserProfile activatedProfile = existingProfile.activate();
        userProfileRepository.save(activatedProfile);
        log.info("User profile activated successfully with ID: {}", userId.getValue());
    }

    @Override
    public UserProfile updateImpactScore(UserId userId, int impactScore) {
        log.info("Updating impact score for user ID: {}", userId.getValue());
        
        UserProfile existingProfile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new UserProfileNotFoundException(userId));
        
        ImpactScore newScore = new ImpactScore(impactScore);
        UserProfile updatedProfile = existingProfile.addImpactScore(newScore);
        UserProfile savedProfile = userProfileRepository.save(updatedProfile);
        log.info("Impact score updated successfully for user ID: {}", userId.getValue());
        
        return savedProfile;
    }

    @Override
    public boolean existsByEmail(String email) {
        log.debug("Checking if user profile exists by email: {}", email);
        List<UserProfile> allProfiles = userProfileRepository.findAll();
        return allProfiles.stream()
                .anyMatch(profile -> profile.getEmail().getValue().equalsIgnoreCase(email));
    }

    @Override
    public boolean existsByKeycloakId(String keycloakId) {
        log.debug("Checking if user profile exists by Keycloak ID: {}", keycloakId);
        List<UserProfile> allProfiles = userProfileRepository.findAll();
        return allProfiles.stream()
                .anyMatch(profile -> profile.getKeycloakId().equalsIgnoreCase(keycloakId));
    }
}