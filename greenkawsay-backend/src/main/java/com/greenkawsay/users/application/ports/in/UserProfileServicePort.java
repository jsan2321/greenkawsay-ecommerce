package com.greenkawsay.users.application.ports.in;

import com.greenkawsay.users.domain.models.UserProfile;
import com.greenkawsay.users.domain.valueobjects.UserId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service port interface for UserProfile operations
 */
public interface UserProfileServicePort {
    
    /**
     * Create a new user profile
     */
    UserProfile createUserProfile(UserProfile userProfile);
    
    /**
     * Update an existing user profile
     */
    UserProfile updateUserProfile(UserId userId, UserProfile userProfile);
    
    /**
     * Get user profile by ID
     */
    Optional<UserProfile> getUserProfileById(UserId userId);
    
    /**
     * Get user profile by email
     */
    Optional<UserProfile> getUserProfileByEmail(String email);
    
    /**
     * Get user profile by Keycloak ID
     */
    Optional<UserProfile> getUserProfileByKeycloakId(String keycloakId);
    
    /**
     * Get all user profiles with pagination
     */
    Page<UserProfile> getAllUserProfiles(Pageable pageable);
    
    /**
     * Get user profiles by role with pagination
     */
    Page<UserProfile> getUserProfilesByRole(String role, Pageable pageable);
    
    /**
     * Deactivate user profile
     */
    void deactivateUserProfile(UserId userId);
    
    /**
     * Activate user profile
     */
    void activateUserProfile(UserId userId);
    
    /**
     * Update user impact score
     */
    UserProfile updateImpactScore(UserId userId, int impactScore);
    
    /**
     * Check if user profile exists
     */
    boolean existsByEmail(String email);
    
    /**
     * Check if user profile exists by Keycloak ID
     */
    boolean existsByKeycloakId(String keycloakId);
}