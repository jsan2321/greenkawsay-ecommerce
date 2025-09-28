package com.greenkawsay.users.infrastructure.adapters.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for creating a new wishlist
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateWishlistRequest {

    @NotBlank(message = "Wishlist name is required")
    @Size(max = 100, message = "Wishlist name cannot exceed 100 characters")
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotNull(message = "Public flag is required")
    private Boolean isPublic = false;
}