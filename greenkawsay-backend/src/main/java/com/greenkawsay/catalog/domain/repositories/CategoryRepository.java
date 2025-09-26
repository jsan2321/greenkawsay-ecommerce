package com.greenkawsay.catalog.domain.repositories;

import com.greenkawsay.catalog.domain.models.Category;
import com.greenkawsay.catalog.domain.valueobjects.CategoryId;

import java.util.List;
import java.util.Optional;

/**
 * Domain repository interface for Category operations
 * Defines the contract for category data access in the domain layer
 */
public interface CategoryRepository {
    
    /**
     * Save a category
     */
    Category save(Category category);
    
    /**
     * Find category by ID
     */
    Optional<Category> findById(CategoryId categoryId);
    
    /**
     * Find category by slug
     */
    Optional<Category> findBySlug(String slug);
    
    /**
     * Find all root categories (categories without parent)
     */
    List<Category> findRootCategories();
    
    /**
     * Find all subcategories of a parent category
     */
    List<Category> findByParentId(CategoryId parentId);
    
    /**
     * Find categories by name containing (case-insensitive)
     */
    List<Category> findByNameContaining(String name);
    
    /**
     * Check if category with given slug exists
     */
    boolean existsBySlug(String slug);
    
    /**
     * Find active categories only
     */
    List<Category> findActiveCategories();
    
    /**
     * Find active categories by parent
     */
    List<Category> findActiveCategoriesByParent(CategoryId parentId);
    
    /**
     * Delete category by ID
     */
    void deleteById(CategoryId categoryId);
    
    /**
     * Count products in category (including subcategories)
     */
    Long countProductsInCategory(CategoryId categoryId);
}