package com.greenkawsay.catalog.infrastructure.adapters.in.web.controllers;

import com.greenkawsay.catalog.application.commands.CreateProductCommand;
import com.greenkawsay.catalog.application.commands.UpdateProductCommand;
import com.greenkawsay.catalog.application.ports.in.ProductServicePort;
import com.greenkawsay.catalog.domain.models.Product;
import com.greenkawsay.catalog.domain.valueobjects.CategoryId;
import com.greenkawsay.catalog.domain.valueobjects.ProductId;
import com.greenkawsay.catalog.domain.valueobjects.StockQuantity;
import com.greenkawsay.catalog.infrastructure.adapters.in.web.dto.request.CreateProductRequest;
import com.greenkawsay.catalog.infrastructure.adapters.in.web.dto.request.UpdateProductRequest;
import com.greenkawsay.catalog.infrastructure.adapters.in.web.mappers.ProductMapper;
import com.greenkawsay.shared.domain.valueobjects.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerUnitTest {

    @Mock
    private ProductServicePort productService;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductController productController;

    private Product testProduct;
    private ProductId productId;
    private UUID categoryId;
    private UUID userId;

    @BeforeEach
    void setUp() {
        productId = new ProductId(UUID.randomUUID());
        categoryId = UUID.randomUUID();
        userId = UUID.randomUUID();

        testProduct = new Product(
                productId,
                "Test Product",
                "Test Description",
                new Money(new BigDecimal("29.99"), "USD"),
                new CategoryId(categoryId),
                new StockQuantity(100),
                true,
                userId,
                LocalDateTime.now(),
                LocalDateTime.now(),
                userId,
                userId
        );
    }

    @Test
    void createProduct_ShouldReturnCreatedProduct() {
        // Arrange
        CreateProductRequest request = new CreateProductRequest();
        request.setName("Test Product");
        request.setDescription("Test Description");
        request.setPrice(new BigDecimal("29.99"));
        request.setCategoryId(categoryId);
        request.setUserId(userId);
        request.setStockQuantity(100);

        CreateProductCommand command = new CreateProductCommand(
                "Test Product", "Test Description", new Money(new BigDecimal("29.99"), "USD"),
                new CategoryId(categoryId), new StockQuantity(100), userId.toString(), true
        );

        when(productMapper.toCreateProductCommand(request, userId)).thenReturn(command);
        when(productService.createProduct(command)).thenReturn(testProduct);

        // Act
        ResponseEntity<?> response = productController.createProduct(request, userId);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(productService).createProduct(command);
    }

    @Test
    void getProductById_ShouldReturnProduct() {
        // Arrange
        when(productService.getProductById(productId)).thenReturn(testProduct);

        // Act
        ResponseEntity<?> response = productController.getProductById(productId.getValue());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(productService).getProductById(productId);
    }

    @Test
    void getAllProducts_ShouldReturnPaginatedProducts() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name"));
        Page<Product> productPage = new PageImpl<>(List.of(testProduct), pageable, 1);
        
        when(productService.getAllProducts(pageable)).thenReturn(productPage);

        // Act
        ResponseEntity<?> response = productController.getAllProducts(0, 10, "name", "asc", null, null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(productService).getAllProducts(pageable);
    }

    @Test
    void updateProduct_ShouldReturnUpdatedProduct() {
        // Arrange
        UpdateProductRequest request = new UpdateProductRequest();
        request.setName("Updated Product");
        request.setDescription("Updated Description");
        request.setPrice(new BigDecimal("39.99"));

        UpdateProductCommand command = new UpdateProductCommand(
                "Updated Product", "Updated Description", new Money(new BigDecimal("39.99"), "USD"),
                new CategoryId(categoryId), new StockQuantity(50), true
        );

        when(productMapper.toUpdateProductCommand(request, productId.getValue(), userId)).thenReturn(command);
        when(productService.updateProduct(productId, command)).thenReturn(testProduct);

        // Act
        ResponseEntity<?> response = productController.updateProduct(productId.getValue(), request, userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(productService).updateProduct(productId, command);
    }

    @Test
    void updateStock_ShouldReturnUpdatedProduct() {
        // Arrange
        when(productService.updateStock(productId.getValue(), 50, userId)).thenReturn(testProduct);

        // Act
        ResponseEntity<?> response = productController.updateStock(productId.getValue(), 50, userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(productService).updateStock(productId.getValue(), 50, userId);
    }

    @Test
    void deleteProduct_ShouldReturnNoContent() {
        // Arrange
        doNothing().when(productService).deleteProduct(productId.getValue());

        // Act
        ResponseEntity<?> response = productController.deleteProduct(productId.getValue());

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(productService).deleteProduct(productId.getValue());
    }

    @Test
    void getProductsByCategory_ShouldReturnProducts() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
        Page<Product> productPage = new PageImpl<>(List.of(testProduct), pageable, 1);
        
        when(productService.getProductsByCategory(categoryId, pageable)).thenReturn(productPage);

        // Act
        ResponseEntity<?> response = productController.getProductsByCategory(categoryId, 0, 10);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(productService).getProductsByCategory(categoryId, pageable);
    }

    @Test
    void searchProducts_ShouldReturnMatchingProducts() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
        Page<Product> productPage = new PageImpl<>(List.of(testProduct), pageable, 1);
        
        when(productService.searchProductsByName("test", pageable)).thenReturn(productPage);

        // Act
        ResponseEntity<?> response = productController.searchProducts("test", 0, 10);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(productService).searchProductsByName("test", pageable);
    }

    @Test
    void searchProductsByCategoryAndName_ShouldReturnMatchingProducts() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name"));
        Page<Product> productPage = new PageImpl<>(List.of(testProduct), pageable, 1);
        
        when(productService.searchProductsByCategoryAndName(categoryId, "test", pageable)).thenReturn(productPage);

        // Act
        ResponseEntity<?> response = productController.getAllProducts(0, 10, "name", "asc", categoryId, "test");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(productService).searchProductsByCategoryAndName(categoryId, "test", pageable);
    }
}