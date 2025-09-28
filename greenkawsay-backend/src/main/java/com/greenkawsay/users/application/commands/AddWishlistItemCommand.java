package com.greenkawsay.users.application.commands;

import com.greenkawsay.catalog.domain.valueobjects.ProductId;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Command DTO for adding an item to a wishlist
 */
public record AddWishlistItemCommand(
    
    @NotNull(message = "Product ID is required")
    ProductId productId
) {
    
    public AddWishlistItemCommand(UUID productId) {
        this(new ProductId(productId));
    }
}