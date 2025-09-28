package com.greenkawsay.users.domain.repositories;

import com.greenkawsay.catalog.domain.valueobjects.ProductId;
import com.greenkawsay.users.domain.models.WishlistItem;
import com.greenkawsay.users.domain.valueobjects.WishlistId;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for WishlistItem domain entities
 * Defines the contract for persistence operations
 */
public interface WishlistItemRepository {
    
    /**
     * Save a wishlist item
     */
    WishlistItem save(WishlistItem wishlistItem);
    
    /**
     * Find wishlist item by wishlist ID and product ID
     */
    Optional<WishlistItem> findByWishlistIdAndProductId(WishlistId wishlistId, ProductId productId);
    
    /**
     * Find wishlist items by wishlist ID
     */
    List<WishlistItem> findByWishlistId(WishlistId wishlistId);
    
    /**
     * Find wishlist items by product ID
     */
    List<WishlistItem> findByProductId(ProductId productId);
    
    /**
     * Find recently added wishlist items
     */
    List<WishlistItem> findRecentlyAdded(int daysThreshold);
    
    /**
     * Check if wishlist item exists
     */
    boolean existsByWishlistIdAndProductId(WishlistId wishlistId, ProductId productId);
    
    /**
     * Delete wishlist item by wishlist ID and product ID
     */
    void deleteByWishlistIdAndProductId(WishlistId wishlistId, ProductId productId);
    
    /**
     * Delete wishlist items by wishlist ID
     */
    void deleteByWishlistId(WishlistId wishlistId);
    
    /**
     * Delete wishlist items by product ID
     */
    void deleteByProductId(ProductId productId);
    
    /**
     * Count wishlist items in wishlist
     */
    long countByWishlistId(WishlistId wishlistId);
    
    /**
     * Count wishlist items for product
     */
    long countByProductId(ProductId productId);
}