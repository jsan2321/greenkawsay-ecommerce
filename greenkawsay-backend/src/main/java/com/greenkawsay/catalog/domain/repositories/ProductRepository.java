package com.greenkawsay.catalog.domain.repositories;

import com.greenkawsay.catalog.domain.models.Product;
import com.greenkawsay.catalog.domain.valueobjects.CategoryId;
import com.greenkawsay.catalog.domain.valueobjects.ProductId;
import com.greenkawsay.shared.domain.valueobjects.Money;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Domain repository interface for Product operations
 * Defines the contract for product data access in the domain layer
 */
public interface ProductRepository {
    
    /**
     * Save a product
     */
    Product save(Product product);
    
    /**
     * Find product by ID
     */
    Optional<Product> findById(ProductId productId);
    
    /**
     * Find products by category
     */
    List<Product> findByCategoryId(CategoryId categoryId);
    
    /**
     * Find products by category including subcategories
     */
    List<Product> findByCategoryAndSubcategories(CategoryId categoryId);
    
    /**
     * Find products by user (vendor)
     */
    List<Product> findByUserId(String userId);
    
    /**
     * Find active products only
     */
    List<Product> findActiveProducts();
    
    /**
     * Find active products by category
     */
    List<Product> findActiveProductsByCategory(CategoryId categoryId);
    
    /**
     * Find products by name containing (case-insensitive)
     */
    List<Product> findByNameContaining(String name);
    
    /**
     * Find active products by name containing (case-insensitive)
     */
    List<Product> findActiveProductsByNameContaining(String name);
    
    /**
     * Find products by price range
     */
    List<Product> findByPriceRange(Money minPrice, Money maxPrice);
    
    /**
     * Find active products by price range
     */
    List<Product> findActiveProductsByPriceRange(Money minPrice, Money maxPrice);
    
    /**
     * Find products with stock greater than zero
     */
    List<Product> findProductsWithStock();
    
    /**
     * Find active products with stock greater than zero
     */
    List<Product> findActiveProductsWithStock();
    
    /**
     * Find products by multiple categories
     */
    List<Product> findByCategoryIds(List<CategoryId> categoryIds);
    
    /**
     * Find active products by multiple categories
     */
    List<Product> findActiveProductsByCategoryIds(List<CategoryId> categoryIds);
    
    /**
     * Check if product with given name exists
     */
    boolean existsByName(String name);
    
    /**
     * Count products by category
     */
    Long countByCategoryId(CategoryId categoryId);
    
    /**
     * Count active products by category
     */
    Long countActiveProductsByCategoryId(CategoryId categoryId);
    
    /**
     * Find featured products
     */
    List<Product> findFeaturedProducts();
    
    /**
     * Find products with low stock (below threshold)
     */
    List<Product> findLowStockProducts(int threshold);
    
    /**
     * Delete product by ID
     */
    void deleteById(ProductId productId);
    
    // Pagination methods
    /**
     * Find all products with pagination
     */
    Page<Product> findAll(Pageable pageable);
    
    /**
     * Find products by category with pagination
     */
    Page<Product> findByCategoryId(CategoryId categoryId, Pageable pageable);
    
    /**
     * Find active products by category with pagination
     */
    Page<Product> findActiveProductsByCategory(CategoryId categoryId, Pageable pageable);
    
    /**
     * Find products by name containing with pagination
     */
    Page<Product> findByNameContaining(String name, Pageable pageable);
    
    /**
     * Find active products by name containing with pagination
     */
    Page<Product> findActiveProductsByNameContaining(String name, Pageable pageable);
    
    /**
     * Find products by category and name with pagination
     */
    Page<Product> findByCategoryIdAndNameContaining(CategoryId categoryId, String name, Pageable pageable);
    
    /**
     * Find active products by category and name with pagination
     */
    Page<Product> findActiveProductsByCategoryAndNameContaining(CategoryId categoryId, String name, Pageable pageable);
}