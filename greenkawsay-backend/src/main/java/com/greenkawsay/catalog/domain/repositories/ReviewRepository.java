package com.greenkawsay.catalog.domain.repositories;

import com.greenkawsay.catalog.domain.models.Review;
import com.greenkawsay.catalog.domain.valueobjects.ProductId;

import java.util.List;
import java.util.Optional;

/**
 * Domain repository interface for Review operations
 * Defines the contract for review data access in the domain layer
 */
public interface ReviewRepository {
    
    /**
     * Save a review
     */
    Review save(Review review);
    
    /**
     * Find review by ID
     */
    Optional<Review> findById(String reviewId);
    
    /**
     * Find reviews by product
     */
    List<Review> findByProductId(ProductId productId);
    
    /**
     * Find reviews by user
     */
    List<Review> findByUserId(String userId);
    
    /**
     * Find review by user and product
     */
    Optional<Review> findByUserIdAndProductId(String userId, ProductId productId);
    
    /**
     * Find reviews by rating
     */
    List<Review> findByRating(int rating);
    
    /**
     * Find reviews by rating range
     */
    List<Review> findByRatingBetween(int minRating, int maxRating);
    
    /**
     * Find reviews with comments
     */
    List<Review> findReviewsWithComments();
    
    /**
     * Find reviews for a product with comments
     */
    List<Review> findReviewsWithCommentsByProductId(ProductId productId);
    
    /**
     * Calculate average rating for a product
     */
    Double calculateAverageRatingByProductId(ProductId productId);
    
    /**
     * Count reviews by product
     */
    Long countByProductId(ProductId productId);
    
    /**
     * Count reviews by rating for a product
     */
    Long countByProductIdAndRating(ProductId productId, int rating);
    
    /**
     * Find latest reviews
     */
    List<Review> findLatestReviews();
    
    /**
     * Find latest reviews for a product
     */
    List<Review> findLatestReviewsByProductId(ProductId productId);
    
    /**
     * Find reviews by multiple products
     */
    List<Review> findByProductIds(List<ProductId> productIds);
    
    /**
     * Check if user has reviewed a product
     */
    boolean existsByUserIdAndProductId(String userId, ProductId productId);
    
    /**
     * Delete reviews by product
     */
    void deleteByProductId(ProductId productId);
    
    /**
     * Delete reviews by user
     */
    void deleteByUserId(String userId);
    
    /**
     * Delete review by ID
     */
    void deleteById(String reviewId);
}