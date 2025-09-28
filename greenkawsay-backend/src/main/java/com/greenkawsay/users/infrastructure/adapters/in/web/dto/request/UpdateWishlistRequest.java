package com.greenkawsay.users.infrastructure.adapters.in.web.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for updating an existing wishlist
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateWishlistRequest {

    @Size(max = 100, message = "Wishlist name cannot exceed 100 characters")
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    private Boolean isPublic;
}