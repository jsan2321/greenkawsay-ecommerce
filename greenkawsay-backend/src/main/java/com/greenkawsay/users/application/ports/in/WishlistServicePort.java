package com.greenkawsay.users.application.ports.in;

import com.greenkawsay.catalog.domain.valueobjects.ProductId;
import com.greenkawsay.users.domain.models.Wishlist;
import com.greenkawsay.users.domain.models.WishlistItem;
import com.greenkawsay.users.domain.valueobjects.UserId;
import com.greenkawsay.users.domain.valueobjects.WishlistId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service port interface for Wishlist operations
 */
public interface WishlistServicePort {
    
    /**
     * Create a new wishlist for a user
     */
    Wishlist createWishlist(Wishlist wishlist);
    
    /**
     * Update an existing wishlist
     */
    Wishlist updateWishlist(WishlistId wishlistId, Wishlist wishlist);
    
    /**
     * Get wishlist by ID
     */
    Optional<Wishlist> getWishlistById(WishlistId wishlistId);
    
    /**
     * Get wishlists by user ID
     */
    List<Wishlist> getWishlistsByUserId(UserId userId);
    
    /**
     * Get wishlists by user ID with pagination
     */
    Page<Wishlist> getWishlistsByUserId(UserId userId, Pageable pageable);
    
    /**
     * Get default wishlist for a user
     */
    Optional<Wishlist> getDefaultWishlistByUserId(UserId userId);
    
    /**
     * Set a wishlist as default for a user
     */
    Wishlist setDefaultWishlist(WishlistId wishlistId);
    
    /**
     * Delete a wishlist
     */
    void deleteWishlist(WishlistId wishlistId);
    
    /**
     * Add item to wishlist
     */
    WishlistItem addItemToWishlist(WishlistId wishlistId, ProductId productId);
    
    /**
     * Remove item from wishlist
     */
    void removeItemFromWishlist(WishlistId wishlistId, ProductId productId);
    
    /**
     * Get wishlist items
     */
    List<WishlistItem> getWishlistItems(WishlistId wishlistId);
    
    /**
     * Get wishlist items with pagination
     */
    Page<WishlistItem> getWishlistItems(WishlistId wishlistId, Pageable pageable);
    
    /**
     * Check if product is in wishlist
     */
    boolean isProductInWishlist(WishlistId wishlistId, ProductId productId);
    
    /**
     * Get wishlist item by product ID
     */
    Optional<WishlistItem> getWishlistItemByProductId(WishlistId wishlistId, ProductId productId);
    
    /**
     * Get public wishlists
     */
    Page<Wishlist> getPublicWishlists(Pageable pageable);
    
    /**
     * Get wishlists by name containing (search)
     */
    Page<Wishlist> getWishlistsByNameContaining(String name, Pageable pageable);
    
    /**
     * Check if wishlist exists
     */
    boolean existsById(WishlistId wishlistId);
    
    /**
     * Check if user has any wishlists
     */
    boolean hasWishlists(UserId userId);
}