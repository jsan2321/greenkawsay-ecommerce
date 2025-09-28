package com.greenkawsay.users.application.commands;

import jakarta.validation.constraints.Min;

/**
 * Command DTO for updating user impact score
 */
public record UpdateImpactScoreCommand(
    
    @Min(value = 0, message = "Impact score cannot be negative")
    int impactScoreTotal
) {
}