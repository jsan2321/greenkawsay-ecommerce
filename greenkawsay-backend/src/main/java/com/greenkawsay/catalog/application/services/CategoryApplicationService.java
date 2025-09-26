package com.greenkawsay.catalog.application.services;

import com.greenkawsay.catalog.application.ports.in.CategoryServicePort;
import com.greenkawsay.catalog.domain.exceptions.CategoryNotFoundException;
import com.greenkawsay.catalog.domain.models.Category;
import com.greenkawsay.catalog.domain.repositories.CategoryRepository;
import com.greenkawsay.catalog.domain.valueobjects.CategoryId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Application service for category use cases
 * Implements the CategoryServicePort interface
 */
@Service
@Transactional
public class CategoryApplicationService implements CategoryServicePort {
    
    private final CategoryRepository categoryRepository;
    
    public CategoryApplicationService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    
    @Override
    public Category createCategory(String name, CategoryId parentId) {
        // Validate business rules
        validateCategoryCreation(name, parentId);
        
        // Generate slug from name
        String slug = generateSlug(name);
        
        // Create the category domain entity
        Category category = new Category(
            name,
            slug,
            null, // description
            parentId,
            UUID.randomUUID() // In a real app, this would come from authentication context
        );
        
        // Save and return
        return categoryRepository.save(category);
    }
    
    @Override
    public List<Category> getCategoryTree() {
        // Get all categories
        List<Category> allCategories = categoryRepository.findActiveCategories();
        
        // Build hierarchical structure
        return buildCategoryTree(allCategories);
    }
    
    @Override
    public Category getCategoryById(CategoryId categoryId) {
        return categoryRepository.findById(categoryId)
            .orElseThrow(() -> new CategoryNotFoundException(categoryId));
    }
    
    @Override
    public Category getCategoryBySlug(String slug) {
        return categoryRepository.findBySlug(slug)
            .orElseThrow(() -> new CategoryNotFoundException(slug));
    }
    
    @Override
    public Category updateCategory(CategoryId categoryId, String name, CategoryId parentId) {
        // Find the category
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new CategoryNotFoundException(categoryId));
        
        // Validate parent category exists if provided
        if (parentId != null && !categoryRepository.findById(parentId).isPresent()) {
            throw new CategoryNotFoundException(parentId);
        }
        
        // Update category information
        category.updateName(name, UUID.randomUUID()); // In real app, use authenticated user
        if (parentId != null) {
            category.changeParent(parentId, UUID.randomUUID());
        }
        
        // Save and return
        return categoryRepository.save(category);
    }
    
    @Override
    public void deleteCategory(CategoryId categoryId) {
        // Check if category exists
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new CategoryNotFoundException(categoryId));
        
        // Check if category has products
        if (categoryRepository.countProductsInCategory(categoryId) > 0) {
            throw new IllegalStateException("Cannot delete category with products");
        }
        
        // Check if category has subcategories
        if (!categoryRepository.findByParentId(categoryId).isEmpty()) {
            throw new IllegalStateException("Cannot delete category with subcategories");
        }
        
        // Delete the category
        categoryRepository.deleteById(categoryId);
    }
    
    @Override
    public List<Category> getAllActiveCategories() {
        return categoryRepository.findActiveCategories();
    }
    
    @Override
    public List<Category> getSubcategories(CategoryId parentId) {
        // Validate parent exists
        if (!categoryRepository.findById(parentId).isPresent()) {
            throw new CategoryNotFoundException(parentId);
        }
        
        return categoryRepository.findByParentId(parentId);
    }
    
    @Override
    public List<Category> getRootCategories() {
        return categoryRepository.findRootCategories();
    }
    
    /**
     * Validate category creation business rules
     */
    private void validateCategoryCreation(String name, CategoryId parentId) {
        // Check for duplicate category slug
        String slug = generateSlug(name);
        if (categoryRepository.existsBySlug(slug)) {
            throw new IllegalStateException("Category with name '" + name + "' already exists");
        }
        
        // Validate parent category exists if provided
        if (parentId != null && !categoryRepository.findById(parentId).isPresent()) {
            throw new CategoryNotFoundException(parentId);
        }
    }
    
    /**
     * Build hierarchical category tree from flat list
     */
    private List<Category> buildCategoryTree(List<Category> categories) {
        // Find root categories (categories without parent)
        List<Category> rootCategories = categories.stream()
            .filter(category -> category.getParentId() == null)
            .toList();
        
        // Build tree recursively
        return rootCategories.stream()
            .map(root -> buildCategoryNode(root, categories))
            .toList();
    }
    
    /**
     * Build a category node with its subcategories
     */
    private Category buildCategoryNode(Category category, List<Category> allCategories) {
        // Find subcategories
        List<Category> subcategories = allCategories.stream()
            .filter(sub -> category.getId().equals(sub.getParentId()))
            .map(sub -> buildCategoryNode(sub, allCategories))
            .toList();
        
        // Return category with subcategories (in a real implementation, we might need to clone or create a tree structure)
        return category; // For now, return the category as-is
    }
    
    /**
     * Generate slug from category name
     */
    private String generateSlug(String name) {
        return name.toLowerCase()
            .replaceAll("[^a-z0-9\\s-]", "") // Remove special characters
            .replaceAll("\\s+", "-") // Replace spaces with hyphens
            .replaceAll("-+", "-") // Replace multiple hyphens with single
            .trim();
    }
}