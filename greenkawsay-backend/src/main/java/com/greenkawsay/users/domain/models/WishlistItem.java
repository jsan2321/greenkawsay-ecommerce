package com.greenkawsay.users.domain.models;

import com.greenkawsay.catalog.domain.valueobjects.ProductId;
import com.greenkawsay.users.domain.valueobjects.WishlistId;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Domain model representing an item in a wishlist
 * Immutable and thread-safe
 */
public class WishlistItem {
    private final WishlistId wishlistId;
    private final ProductId productId;
    private final LocalDateTime addedAt;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public WishlistItem(WishlistId wishlistId, ProductId productId, LocalDateTime addedAt,
                       LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.wishlistId = Objects.requireNonNull(wishlistId, "Wishlist ID cannot be null");
        this.productId = Objects.requireNonNull(productId, "Product ID cannot be null");
        this.addedAt = Objects.requireNonNull(addedAt, "Added at cannot be null");
        this.createdAt = Objects.requireNonNull(createdAt, "Created at cannot be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "Updated at cannot be null");
    }

    // Getters
    public WishlistId getWishlistId() { return wishlistId; }
    public ProductId getProductId() { return productId; }
    public LocalDateTime getAddedAt() { return addedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Business methods
    public WishlistItem updateAddedAt(LocalDateTime newAddedAt) {
        return new WishlistItem(
            this.wishlistId, this.productId, newAddedAt, this.createdAt, LocalDateTime.now()
        );
    }

    public boolean isRecentlyAdded(int daysThreshold) {
        LocalDateTime thresholdDate = LocalDateTime.now().minusDays(daysThreshold);
        return addedAt.isAfter(thresholdDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WishlistItem that = (WishlistItem) o;
        return Objects.equals(wishlistId, that.wishlistId) && 
               Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(wishlistId, productId);
    }

    @Override
    public String toString() {
        return "WishlistItem{" +
                "wishlistId=" + wishlistId +
                ", productId=" + productId +
                ", addedAt=" + addedAt +
                '}';
    }
}