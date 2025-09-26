package com.greenkawsay.catalog.application.services;

import com.greenkawsay.catalog.application.commands.CreateProductCommand;
import com.greenkawsay.catalog.application.commands.UpdateProductCommand;
import com.greenkawsay.catalog.application.commands.UpdateStockCommand;
import com.greenkawsay.catalog.domain.exceptions.CategoryNotFoundException;
import com.greenkawsay.catalog.domain.exceptions.DuplicateProductException;
import com.greenkawsay.catalog.domain.exceptions.InvalidStockQuantityException;
import com.greenkawsay.catalog.domain.exceptions.ProductNotFoundException;
import com.greenkawsay.catalog.domain.models.Category;
import com.greenkawsay.catalog.domain.models.Product;
import com.greenkawsay.catalog.domain.repositories.CategoryRepository;
import com.greenkawsay.catalog.domain.repositories.ProductRepository;
import com.greenkawsay.catalog.domain.valueobjects.CategoryId;
import com.greenkawsay.catalog.domain.valueobjects.ProductId;
import com.greenkawsay.catalog.domain.valueobjects.StockQuantity;
import com.greenkawsay.shared.domain.valueobjects.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ProductApplicationService
 */
@ExtendWith(MockitoExtension.class)
class ProductApplicationServiceTest {
    
    @Mock
    private ProductRepository productRepository;
    
    @Mock
    private CategoryRepository categoryRepository;
    
    @InjectMocks
    private ProductApplicationService productService;
    
    private CategoryId categoryId;
    private Category category;
    private Product product;
    private UUID userId;
    
    @BeforeEach
    void setUp() {
        categoryId = CategoryId.generate();
        userId = UUID.randomUUID();
        
        category = new Category(
            categoryId,
            "Eco-Friendly Products",
            "eco-friendly-products",
            "Environmentally friendly products",
            null, // parentId
            java.time.LocalDateTime.now(),
            java.time.LocalDateTime.now(),
            userId,
            userId
        );
        
        product = new Product(
            ProductId.generate(),
            "Organic Cotton T-Shirt",
            "100% organic cotton t-shirt",
            Money.ofPEN(25.99),
            categoryId,
            StockQuantity.of(50),
            true,
            userId,
            java.time.LocalDateTime.now(),
            java.time.LocalDateTime.now(),
            userId,
            userId
        );
    }
    
    @Test
    void createProduct_ShouldCreateProduct_WhenValidCommand() {
        // Arrange
        CreateProductCommand command = new CreateProductCommand(
            "Bamboo Toothbrush",
            "Eco-friendly bamboo toothbrush",
            Money.ofPEN(12.50),
            categoryId,
            StockQuantity.of(100),
            userId.toString(),
            true
        );
        
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(productRepository.existsByName("Bamboo Toothbrush")).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // Act
        Product result = productService.createProduct(command);
        
        // Assert
        assertNotNull(result);
        assertEquals("Bamboo Toothbrush", result.getName());
        assertEquals("Eco-friendly bamboo toothbrush", result.getDescription());
        assertEquals(Money.ofPEN(12.50), result.getPrice());
        assertEquals(categoryId, result.getCategoryId());
        assertEquals(StockQuantity.of(100), result.getStockQuantity());
        assertTrue(result.isActive());
        
        verify(categoryRepository).findById(categoryId);
        verify(productRepository).existsByName("Bamboo Toothbrush");
        verify(productRepository).save(any(Product.class));
    }
    
    @Test
    void createProduct_ShouldThrowException_WhenCategoryNotFound() {
        // Arrange
        CreateProductCommand command = new CreateProductCommand(
            "Bamboo Toothbrush",
            "Eco-friendly bamboo toothbrush",
            Money.ofPEN(12.50),
            categoryId,
            StockQuantity.of(100),
            userId.toString(),
            true
        );
        
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(CategoryNotFoundException.class, () -> productService.createProduct(command));
        
        verify(categoryRepository).findById(categoryId);
        verify(productRepository, never()).save(any(Product.class));
    }
    
    @Test
    void createProduct_ShouldThrowException_WhenDuplicateProductName() {
        // Arrange
        CreateProductCommand command = new CreateProductCommand(
            "Bamboo Toothbrush",
            "Eco-friendly bamboo toothbrush",
            Money.ofPEN(12.50),
            categoryId,
            StockQuantity.of(100),
            userId.toString(),
            true
        );
        
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(productRepository.existsByName("Bamboo Toothbrush")).thenReturn(true);
        
        // Act & Assert
        assertThrows(DuplicateProductException.class, () -> productService.createProduct(command));
        
        verify(categoryRepository).findById(categoryId);
        verify(productRepository).existsByName("Bamboo Toothbrush");
        verify(productRepository, never()).save(any(Product.class));
    }
    
    @Test
    void getProductById_ShouldReturnProduct_WhenProductExists() {
        // Arrange
        ProductId productId = product.getId();
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        
        // Act
        Product result = productService.getProductById(productId);
        
        // Assert
        assertNotNull(result);
        assertEquals(productId, result.getId());
        verify(productRepository).findById(productId);
    }
    
    @Test
    void getProductById_ShouldThrowException_WhenProductNotFound() {
        // Arrange
        ProductId productId = ProductId.generate();
        when(productRepository.findById(productId)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(productId));
        verify(productRepository).findById(productId);
    }
    
    @Test
    void updateStock_ShouldUpdateStock_WhenValidQuantity() {
        // Arrange
        ProductId productId = product.getId();
        int newQuantity = 75;
        
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // Act
        Product result = productService.updateStock(productId.getValue(), newQuantity, userId);
        
        // Assert
        assertNotNull(result);
        assertEquals(StockQuantity.of(75), result.getStockQuantity());
        verify(productRepository).findById(productId);
        verify(productRepository).save(any(Product.class));
    }
    
    @Test
    void updateStock_ShouldThrowException_WhenProductNotFound() {
        // Arrange
        ProductId productId = ProductId.generate();
        int newQuantity = 75;
        
        when(productRepository.findById(productId)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(ProductNotFoundException.class, () -> productService.updateStock(productId.getValue(), newQuantity, userId));
        verify(productRepository).findById(productId);
        verify(productRepository, never()).save(any(Product.class));
    }
    
    @Test
    void updateProduct_ShouldUpdateProduct_WhenValidCommand() {
        // Arrange
        ProductId productId = product.getId();
        CategoryId newCategoryId = CategoryId.generate();
        Category newCategory = new Category(
            newCategoryId,
            "New Category",
            "new-category",
            "New category description",
            null,
            java.time.LocalDateTime.now(),
            java.time.LocalDateTime.now(),
            userId,
            userId
        );
        
        UpdateProductCommand command = new UpdateProductCommand(
            "Updated Product Name",
            "Updated description",
            Money.ofPEN(29.99),
            newCategoryId,
            StockQuantity.of(25),
            false
        );
        
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(categoryRepository.findById(newCategoryId)).thenReturn(Optional.of(newCategory));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // Act
        Product result = productService.updateProduct(productId, command);
        
        // Assert
        assertNotNull(result);
        verify(productRepository).findById(productId);
        verify(categoryRepository).findById(newCategoryId);
        verify(productRepository).save(any(Product.class));
    }
    
    @Test
    void deleteProduct_ShouldDeleteProduct_WhenProductExists() {
        // Arrange
        ProductId productId = product.getId();
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        
        // Act
        productService.deleteProduct(productId.getValue());
        
        // Assert
        verify(productRepository).findById(productId);
        verify(productRepository).deleteById(productId);
    }
    
    @Test
    void getAllActiveProducts_ShouldReturnActiveProducts() {
        // Arrange
        List<Product> activeProducts = List.of(product);
        when(productRepository.findActiveProducts()).thenReturn(activeProducts);
        
        // Act
        List<Product> result = productService.getAllActiveProducts();
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(product, result.get(0));
        verify(productRepository).findActiveProducts();
    }
    
}