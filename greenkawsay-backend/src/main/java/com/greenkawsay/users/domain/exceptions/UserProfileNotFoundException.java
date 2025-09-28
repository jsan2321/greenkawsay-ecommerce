package com.greenkawsay.users.domain.exceptions;

import com.greenkawsay.shared.domain.exceptions.DomainException;
import com.greenkawsay.users.domain.valueobjects.UserId;

/**
 * Exception thrown when a user profile is not found
 */
public class UserProfileNotFoundException extends DomainException {
    
    public UserProfileNotFoundException(UserId userId) {
        super("User profile not found with ID: " + userId.getValueAsString(), "USER_PROFILE_NOT_FOUND");
    }
    
    public UserProfileNotFoundException(String keycloakId) {
        super("User profile not found with Keycloak ID: " + keycloakId, "USER_PROFILE_NOT_FOUND");
    }
    
    public UserProfileNotFoundException(String message, Throwable cause) {
        super(message, "USER_PROFILE_NOT_FOUND", cause);
    }
}