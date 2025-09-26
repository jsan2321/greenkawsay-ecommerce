package com.greenkawsay.catalog.application.ports.in;

import com.greenkawsay.catalog.domain.models.Category;
import com.greenkawsay.catalog.domain.valueobjects.CategoryId;

import java.util.List;

/**
 * Input port for category-related use cases
 * Defines the contract for category application services
 */
public interface CategoryServicePort {
    
    /**
     * Create a new category with hierarchical validation
     * @param name The category name
     * @param parentId Optional parent category ID
     * @return The created category
     */
    Category createCategory(String name, CategoryId parentId);
    
    /**
     * Get the entire hierarchical category structure
     * @return List of root categories with their subcategories
     */
    List<Category> getCategoryTree();
    
    /**
     * Get category by ID
     * @param categoryId The category ID
     * @return The category
     */
    Category getCategoryById(CategoryId categoryId);
    
    /**
     * Get category by slug
     * @param slug The category slug
     * @return The category
     */
    Category getCategoryBySlug(String slug);
    
    /**
     * Update category information
     * @param categoryId The category ID
     * @param name The new category name
     * @param parentId The new parent category ID
     * @return The updated category
     */
    Category updateCategory(CategoryId categoryId, String name, CategoryId parentId);
    
    /**
     * Delete category by ID
     * @param categoryId The category ID
     */
    void deleteCategory(CategoryId categoryId);
    
    /**
     * Get all active categories
     * @return List of active categories
     */
    List<Category> getAllActiveCategories();
    
    /**
     * Get subcategories of a parent category
     * @param parentId The parent category ID
     * @return List of subcategories
     */
    List<Category> getSubcategories(CategoryId parentId);
    
    /**
     * Get root categories (categories without parent)
     * @return List of root categories
     */
    List<Category> getRootCategories();
}