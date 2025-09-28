package com.greenkawsay.users.domain.repositories;

import com.greenkawsay.users.domain.models.Wishlist;
import com.greenkawsay.users.domain.valueobjects.UserId;
import com.greenkawsay.users.domain.valueobjects.WishlistId;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Wishlist domain entities
 * Defines the contract for persistence operations
 */
public interface WishlistRepository {
    
    /**
     * Save a wishlist
     */
    Wishlist save(Wishlist wishlist);
    
    /**
     * Find wishlist by ID
     */
    Optional<Wishlist> findById(WishlistId wishlistId);
    
    /**
     * Find wishlists by user ID
     */
    List<Wishlist> findByUserId(UserId userId);
    
    /**
     * Find public wishlists
     */
    List<Wishlist> findPublicWishlists();
    
    /**
     * Find wishlists by name (case-insensitive search)
     */
    List<Wishlist> findByNameContainingIgnoreCase(String name);
    
    /**
     * Find wishlists by user ID and name
     */
    List<Wishlist> findByUserIdAndNameContainingIgnoreCase(UserId userId, String name);
    
    /**
     * Check if wishlist exists by ID
     */
    boolean existsById(WishlistId wishlistId);
    
    /**
     * Check if wishlist name exists for user
     */
    boolean existsByUserIdAndName(UserId userId, String name);
    
    /**
     * Delete wishlist by ID
     */
    void deleteById(WishlistId wishlistId);
    
    /**
     * Delete wishlists by user ID
     */
    void deleteByUserId(UserId userId);
    
    /**
     * Count wishlists for user
     */
    long countByUserId(UserId userId);
    
    /**
     * Count public wishlists
     */
    long countPublicWishlists();
}