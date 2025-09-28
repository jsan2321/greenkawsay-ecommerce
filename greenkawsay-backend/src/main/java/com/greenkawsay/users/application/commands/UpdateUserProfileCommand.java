package com.greenkawsay.users.application.commands;

import com.greenkawsay.users.domain.valueobjects.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Command DTO for updating a user profile
 */
public record UpdateUserProfileCommand(
    
    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name must not exceed 100 characters")
    String firstName,
    
    @NotBlank(message = "Last name is required")
    @Size(max = 100, message = "Last name must not exceed 100 characters")
    String lastName,
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    String email,
    
    @NotNull(message = "Role is required")
    Role role,
    
    String avatarUrl,
    
    int impactScoreTotal,
    
    boolean isActive
) {
    
    public UpdateUserProfileCommand {
        if (impactScoreTotal < 0) {
            throw new IllegalArgumentException("Impact score cannot be negative");
        }
    }
}