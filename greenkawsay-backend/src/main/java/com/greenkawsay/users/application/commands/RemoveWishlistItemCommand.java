package com.greenkawsay.users.application.commands;

import com.greenkawsay.catalog.domain.valueobjects.ProductId;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Command DTO for removing an item from a wishlist
 */
public record RemoveWishlistItemCommand(
    
    @NotNull(message = "Product ID is required")
    ProductId productId
) {
    
    public RemoveWishlistItemCommand(UUID productId) {
        this(new ProductId(productId));
    }
}