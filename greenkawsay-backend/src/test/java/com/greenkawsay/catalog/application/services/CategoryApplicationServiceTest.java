package com.greenkawsay.catalog.application.services;

import com.greenkawsay.catalog.domain.exceptions.CategoryNotFoundException;
import com.greenkawsay.catalog.domain.models.Category;
import com.greenkawsay.catalog.domain.repositories.CategoryRepository;
import com.greenkawsay.catalog.domain.valueobjects.CategoryId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for CategoryApplicationService
 */
@ExtendWith(MockitoExtension.class)
class CategoryApplicationServiceTest {
    
    @Mock
    private CategoryRepository categoryRepository;
    
    @InjectMocks
    private CategoryApplicationService categoryService;
    
    private CategoryId categoryId;
    private CategoryId parentId;
    private Category category;
    private Category parentCategory;
    private UUID userId;
    
    @BeforeEach
    void setUp() {
        categoryId = CategoryId.generate();
        parentId = CategoryId.generate();
        userId = UUID.randomUUID();
        
        parentCategory = new Category(
            parentId,
            "Eco-Friendly Products",
            "eco-friendly-products",
            "Environmentally friendly products",
            null, // parentId
            LocalDateTime.now(),
            LocalDateTime.now(),
            userId,
            userId
        );
        
        category = new Category(
            categoryId,
            "Organic Products",
            "organic-products",
            "Organic and natural products",
            parentId,
            LocalDateTime.now(),
            LocalDateTime.now(),
            userId,
            userId
        );
    }
    
    @Test
    void createCategory_ShouldCreateCategory_WhenValidParameters() {
        // Arrange
        String categoryName = "Sustainable Products";
        when(categoryRepository.existsBySlug("sustainable-products")).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // Act
        Category result = categoryService.createCategory(categoryName, null);
        
        // Assert
        assertNotNull(result);
        assertEquals("Sustainable Products", result.getName());
        assertEquals("sustainable-products", result.getSlug());
        assertNull(result.getParentId());
        
        verify(categoryRepository).existsBySlug("sustainable-products");
        verify(categoryRepository).save(any(Category.class));
    }
    
    @Test
    void createCategory_ShouldCreateCategory_WhenWithParent() {
        // Arrange
        String categoryName = "Organic Products";
        when(categoryRepository.existsBySlug("organic-products")).thenReturn(false);
        when(categoryRepository.findById(parentId)).thenReturn(Optional.of(parentCategory));
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // Act
        Category result = categoryService.createCategory(categoryName, parentId);
        
        // Assert
        assertNotNull(result);
        assertEquals("Organic Products", result.getName());
        assertEquals("organic-products", result.getSlug());
        assertEquals(parentId, result.getParentId());
        
        verify(categoryRepository).existsBySlug("organic-products");
        verify(categoryRepository).findById(parentId);
        verify(categoryRepository).save(any(Category.class));
    }
    
    @Test
    void createCategory_ShouldThrowException_WhenDuplicateSlug() {
        // Arrange
        String categoryName = "Eco-Friendly Products";
        when(categoryRepository.existsBySlug("eco-friendly-products")).thenReturn(true);
        
        // Act & Assert
        assertThrows(IllegalStateException.class, () -> categoryService.createCategory(categoryName, null));
        
        verify(categoryRepository).existsBySlug("eco-friendly-products");
        verify(categoryRepository, never()).save(any(Category.class));
    }
    
    @Test
    void createCategory_ShouldThrowException_WhenParentNotFound() {
        // Arrange
        String categoryName = "Organic Products";
        when(categoryRepository.existsBySlug("organic-products")).thenReturn(false);
        when(categoryRepository.findById(parentId)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(CategoryNotFoundException.class, () -> categoryService.createCategory(categoryName, parentId));
        
        verify(categoryRepository).existsBySlug("organic-products");
        verify(categoryRepository).findById(parentId);
        verify(categoryRepository, never()).save(any(Category.class));
    }
    
    @Test
    void getCategoryById_ShouldReturnCategory_WhenCategoryExists() {
        // Arrange
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        
        // Act
        Category result = categoryService.getCategoryById(categoryId);
        
        // Assert
        assertNotNull(result);
        assertEquals(categoryId, result.getId());
        verify(categoryRepository).findById(categoryId);
    }
    
    @Test
    void getCategoryById_ShouldThrowException_WhenCategoryNotFound() {
        // Arrange
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategoryById(categoryId));
        verify(categoryRepository).findById(categoryId);
    }
    
    @Test
    void getCategoryBySlug_ShouldReturnCategory_WhenCategoryExists() {
        // Arrange
        String slug = "organic-products";
        when(categoryRepository.findBySlug(slug)).thenReturn(Optional.of(category));
        
        // Act
        Category result = categoryService.getCategoryBySlug(slug);
        
        // Assert
        assertNotNull(result);
        assertEquals(slug, result.getSlug());
        verify(categoryRepository).findBySlug(slug);
    }
    
    @Test
    void getCategoryBySlug_ShouldThrowException_WhenCategoryNotFound() {
        // Arrange
        String slug = "non-existent-category";
        when(categoryRepository.findBySlug(slug)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategoryBySlug(slug));
        verify(categoryRepository).findBySlug(slug);
    }
    
    @Test
    void updateCategory_ShouldUpdateCategory_WhenValidParameters() {
        // Arrange
        String newName = "Updated Category Name";
        CategoryId newParentId = CategoryId.generate();
        Category newParent = new Category(
            newParentId,
            "New Parent",
            "new-parent",
            "New parent category",
            null,
            LocalDateTime.now(),
            LocalDateTime.now(),
            userId,
            userId
        );
        
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryRepository.findById(newParentId)).thenReturn(Optional.of(newParent));
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // Act
        Category result = categoryService.updateCategory(categoryId, newName, newParentId);
        
        // Assert
        assertNotNull(result);
        verify(categoryRepository).findById(categoryId);
        verify(categoryRepository).findById(newParentId);
        verify(categoryRepository).save(any(Category.class));
    }
    
    @Test
    void updateCategory_ShouldThrowException_WhenCategoryNotFound() {
        // Arrange
        String newName = "Updated Category Name";
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(CategoryNotFoundException.class, () -> 
            categoryService.updateCategory(categoryId, newName, null));
        verify(categoryRepository).findById(categoryId);
        verify(categoryRepository, never()).save(any(Category.class));
    }
    
    @Test
    void deleteCategory_ShouldDeleteCategory_WhenNoProductsOrSubcategories() {
        // Arrange
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryRepository.countProductsInCategory(categoryId)).thenReturn(0L);
        when(categoryRepository.findByParentId(categoryId)).thenReturn(List.of());
        
        // Act
        categoryService.deleteCategory(categoryId);
        
        // Assert
        verify(categoryRepository).findById(categoryId);
        verify(categoryRepository).countProductsInCategory(categoryId);
        verify(categoryRepository).findByParentId(categoryId);
        verify(categoryRepository).deleteById(categoryId);
    }
    
    @Test
    void deleteCategory_ShouldThrowException_WhenCategoryHasProducts() {
        // Arrange
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryRepository.countProductsInCategory(categoryId)).thenReturn(5L);
        
        // Act & Assert
        assertThrows(IllegalStateException.class, () -> categoryService.deleteCategory(categoryId));
        
        verify(categoryRepository).findById(categoryId);
        verify(categoryRepository).countProductsInCategory(categoryId);
        verify(categoryRepository, never()).deleteById(categoryId);
    }
    
    @Test
    void deleteCategory_ShouldThrowException_WhenCategoryHasSubcategories() {
        // Arrange
        Category subcategory = new Category(
            CategoryId.generate(),
            "Subcategory",
            "subcategory",
            "Subcategory description",
            categoryId,
            LocalDateTime.now(),
            LocalDateTime.now(),
            userId,
            userId
        );
        
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryRepository.countProductsInCategory(categoryId)).thenReturn(0L);
        when(categoryRepository.findByParentId(categoryId)).thenReturn(List.of(subcategory));
        
        // Act & Assert
        assertThrows(IllegalStateException.class, () -> categoryService.deleteCategory(categoryId));
        
        verify(categoryRepository).findById(categoryId);
        verify(categoryRepository).countProductsInCategory(categoryId);
        verify(categoryRepository).findByParentId(categoryId);
        verify(categoryRepository, never()).deleteById(categoryId);
    }
    
    @Test
    void getAllActiveCategories_ShouldReturnActiveCategories() {
        // Arrange
        List<Category> activeCategories = List.of(category, parentCategory);
        when(categoryRepository.findActiveCategories()).thenReturn(activeCategories);
        
        // Act
        List<Category> result = categoryService.getAllActiveCategories();
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(categoryRepository).findActiveCategories();
    }
    
    @Test
    void getSubcategories_ShouldReturnSubcategories() {
        // Arrange
        List<Category> subcategories = List.of(category);
        when(categoryRepository.findById(parentId)).thenReturn(Optional.of(parentCategory));
        when(categoryRepository.findByParentId(parentId)).thenReturn(subcategories);
        
        // Act
        List<Category> result = categoryService.getSubcategories(parentId);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(categoryRepository).findById(parentId);
        verify(categoryRepository).findByParentId(parentId);
    }
    
    @Test
    void getRootCategories_ShouldReturnRootCategories() {
        // Arrange
        List<Category> rootCategories = List.of(parentCategory);
        when(categoryRepository.findRootCategories()).thenReturn(rootCategories);
        
        // Act
        List<Category> result = categoryService.getRootCategories();
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(categoryRepository).findRootCategories();
    }
}