package com.greenkawsay.catalog.domain.repositories;

import com.greenkawsay.catalog.domain.models.ProductImage;
import com.greenkawsay.catalog.domain.valueobjects.ProductId;

import java.util.List;
import java.util.Optional;

/**
 * Domain repository interface for ProductImage operations
 * Defines the contract for product image data access in the domain layer
 */
public interface ProductImageRepository {
    
    /**
     * Save a product image
     */
    ProductImage save(ProductImage productImage);
    
    /**
     * Find product image by ID
     */
    Optional<ProductImage> findById(String imageId);
    
    /**
     * Find all images for a product
     */
    List<ProductImage> findByProductId(ProductId productId);
    
    /**
     * Find primary image for a product
     */
    Optional<ProductImage> findPrimaryImageByProductId(ProductId productId);
    
    /**
     * Find all primary images for multiple products
     */
    List<ProductImage> findPrimaryImagesByProductIds(List<ProductId> productIds);
    
    /**
     * Count images for a product
     */
    Long countByProductId(ProductId productId);
    
    /**
     * Check if product has any images
     */
    boolean existsByProductId(ProductId productId);
    
    /**
     * Check if product has a primary image
     */
    boolean hasPrimaryImage(ProductId productId);
    
    /**
     * Find all images by product ordered by creation date
     */
    List<ProductImage> findByProductIdOrderByCreatedAt(ProductId productId);
    
    /**
     * Delete all images for a product
     */
    void deleteByProductId(ProductId productId);
    
    /**
     * Delete specific image by product and image ID
     */
    void deleteByProductIdAndImageId(ProductId productId, String imageId);
    
    /**
     * Delete product image by ID
     */
    void deleteById(String imageId);
}