package com.greenkawsay.users.application.commands;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Command DTO for creating a wishlist
 */
public record CreateWishlistCommand(
    
    @NotBlank(message = "Wishlist name is required")
    @Size(max = 100, message = "Wishlist name must not exceed 100 characters")
    String name,
    
    @Size(max = 500, message = "Description must not exceed 500 characters")
    String description,
    
    boolean isPublic
) {
}