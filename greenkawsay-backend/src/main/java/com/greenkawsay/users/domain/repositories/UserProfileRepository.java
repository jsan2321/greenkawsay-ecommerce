package com.greenkawsay.users.domain.repositories;

import com.greenkawsay.users.domain.models.UserProfile;
import com.greenkawsay.users.domain.valueobjects.UserId;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for UserProfile domain entities
 * Defines the contract for persistence operations
 */
public interface UserProfileRepository {
    
    /**
     * Save a user profile
     */
    UserProfile save(UserProfile userProfile);
    
    /**
     * Find user profile by ID
     */
    Optional<UserProfile> findById(UserId userId);
    
    /**
     * Find user profile by Keycloak ID
     */
    Optional<UserProfile> findByKeycloakId(String keycloakId);
    
    /**
     * Find user profile by email
     */
    Optional<UserProfile> findByEmail(String email);
    
    /**
     * Find all user profiles
     */
    List<UserProfile> findAll();
    
    /**
     * Find active user profiles
     */
    List<UserProfile> findActiveProfiles();
    
    /**
     * Find user profiles by role
     */
    List<UserProfile> findByRole(String role);
    
    /**
     * Check if user profile exists by Keycloak ID
     */
    boolean existsByKeycloakId(String keycloakId);
    
    /**
     * Check if user profile exists by email
     */
    boolean existsByEmail(String email);
    
    /**
     * Delete user profile by ID
     */
    void deleteById(UserId userId);
    
    /**
     * Count total user profiles
     */
    long count();
    
    /**
     * Count active user profiles
     */
    long countActiveProfiles();
}