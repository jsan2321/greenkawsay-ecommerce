package com.greenkawsay.catalog.application.ports.in;

import com.greenkawsay.catalog.application.commands.CreateProductCommand;
import com.greenkawsay.catalog.application.commands.UpdateProductCommand;
import com.greenkawsay.catalog.application.commands.UpdateStockCommand;
import com.greenkawsay.catalog.domain.models.Product;
import com.greenkawsay.catalog.domain.valueobjects.ProductId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * Input port for product-related use cases
 * Defines the contract for product application services
 */
public interface ProductServicePort {
    
    /**
     * Create a new product with business validation
     * @param command The product creation command
     * @return The created product
     */
    Product createProduct(CreateProductCommand command);
    
    /**
     * Get product by ID
     * @param productId The product ID
     * @return The product
     */
    Product getProductById(ProductId productId);
    
    /**
     * Get all products with pagination
     * @param pageable Pagination information
     * @return Page of products
     */
    Page<Product> getAllProducts(Pageable pageable);
    
    /**
     * Get products by category with pagination
     * @param categoryId The category ID
     * @param pageable Pagination information
     * @return Page of products in the category
     */
    Page<Product> getProductsByCategory(UUID categoryId, Pageable pageable);
    
    /**
     * Search products by name with pagination
     * @param searchTerm Search term
     * @param pageable Pagination information
     * @return Page of matching products
     */
    Page<Product> searchProductsByName(String searchTerm, Pageable pageable);
    
    /**
     * Search products by category and name with pagination
     * @param categoryId The category ID
     * @param searchTerm Search term
     * @param pageable Pagination information
     * @return Page of matching products
     */
    Page<Product> searchProductsByCategoryAndName(UUID categoryId, String searchTerm, Pageable pageable);
    
    /**
     * Update product information
     * @param productId The product ID
     * @param command The product update command
     * @return The updated product
     */
    Product updateProduct(ProductId productId, UpdateProductCommand command);
    
    /**
     * Update product stock quantity with validation
     * @param productId The product ID
     * @param quantity New stock quantity
     * @param userId User ID performing the update
     * @return The updated product
     */
    Product updateStock(UUID productId, int quantity, UUID userId);
    
    /**
     * Delete product by ID
     * @param productId The product ID
     */
    void deleteProduct(UUID productId);
    
    /**
     * Get all active products
     * @return List of active products
     */
    List<Product> getAllActiveProducts();
    
    /**
     * Get products by category (legacy method without pagination)
     * @param categoryId The category ID
     * @return List of products in the category
     */
    List<Product> getProductsByCategory(String categoryId);
}