package com.greenkawsay.users.infrastructure.adapters.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for adding an item to a wishlist
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddWishlistItemRequest {

    @NotBlank(message = "Product ID is required")
    private String productId;
}