package com.greenkawsay.catalog.infrastructure.adapters.in.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenkawsay.catalog.application.ports.in.ProductServicePort;
import com.greenkawsay.catalog.domain.models.Product;
import com.greenkawsay.catalog.domain.valueobjects.ProductId;
import com.greenkawsay.catalog.domain.valueobjects.CategoryId;
import com.greenkawsay.catalog.domain.valueobjects.StockQuantity;
import com.greenkawsay.shared.domain.valueobjects.Money;
import com.greenkawsay.catalog.infrastructure.adapters.in.web.dto.request.CreateProductRequest;
import com.greenkawsay.catalog.infrastructure.adapters.in.web.dto.request.UpdateProductRequest;
import com.greenkawsay.catalog.infrastructure.adapters.in.web.dto.request.UpdateStockRequest;
import com.greenkawsay.catalog.infrastructure.adapters.in.web.mappers.ProductMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@ContextConfiguration(classes = ProductController.class)
@ComponentScan(basePackages = "com.greenkawsay.catalog.infrastructure.adapters.in.web.mappers")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
    "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration,org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration"
})
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductServicePort productService;

    @MockitoBean
    private ProductMapper productMapper;

    private Product testProduct;
    private ProductId productId;
    private UUID userId;

    @BeforeEach
    void setUp() {
        productId = ProductId.generate();
        userId = UUID.randomUUID();
        CategoryId categoryId = CategoryId.generate();

        testProduct = new Product(
                productId,
                "Organic Cotton T-Shirt",
                "Sustainable organic cotton t-shirt",
                Money.ofPEN(29.99),
                categoryId,
                StockQuantity.of(100),
                true,
                userId,
                LocalDateTime.now(),
                LocalDateTime.now(),
                userId,
                userId
        );
    }

    @Test
    void createProduct_ShouldReturnCreatedProduct() throws Exception {
        // Arrange
        CreateProductRequest request = new CreateProductRequest(
                "Organic Cotton T-Shirt",
                "Sustainable organic cotton t-shirt",
                new BigDecimal("29.99"),
                UUID.randomUUID(),
                100,
                userId
        );

        when(productService.createProduct(any())).thenReturn(testProduct);
        when(productMapper.toProductResponse(testProduct)).thenReturn(createProductResponse(testProduct));

        // Act & Assert
        mockMvc.perform(post("/api/v1/products")
                                .header("X-User-Id", userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id").value(productId.getValue().toString()))
               .andExpect(jsonPath("$.name").value("Organic Cotton T-Shirt"))
               .andExpect(jsonPath("$.description").value("Sustainable organic cotton t-shirt"))
               .andExpect(jsonPath("$.price").value(29.99))
               .andExpect(jsonPath("$.stockQuantity").value(100));
    }

    @Test
    void getProductById_ShouldReturnProduct() throws Exception {
        // Arrange
        when(productService.getProductById(productId)).thenReturn(testProduct);
        when(productMapper.toProductResponse(testProduct)).thenReturn(createProductResponse(testProduct));

        // Act & Assert
        mockMvc.perform(get("/api/v1/products/{id}", productId.getValue()))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(productId.getValue().toString()))
               .andExpect(jsonPath("$.name").value("Organic Cotton T-Shirt"))
               .andExpect(jsonPath("$.description").value("Sustainable organic cotton t-shirt"));
    }

    @Test
    void getAllProducts_ShouldReturnPaginatedProducts() throws Exception {
        // Arrange
        Page<Product> productPage = new PageImpl<>(List.of(testProduct), PageRequest.of(0, 10), 10);
        when(productService.getAllProducts(any(Pageable.class))).thenReturn(productPage);
        when(productMapper.toProductResponse(testProduct)).thenReturn(createProductResponse(testProduct));

        // Act & Assert
        mockMvc.perform(get("/api/v1/products")
                                .param("page", "0")
                                .param("size", "10"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.products[0].name").value("Organic Cotton T-Shirt"))
               .andExpect(jsonPath("$.totalElements").value(10))
               .andExpect(jsonPath("$.totalPages").value(10));
    }

    @Test
    void updateProduct_ShouldReturnUpdatedProduct() throws Exception {
        // Arrange
        UpdateProductRequest request = new UpdateProductRequest(
                "Updated Organic T-Shirt",
                "Updated description",
                new BigDecimal("34.99"),
                UUID.randomUUID(),
                100,
                userId
        );

        Product updatedProduct = new Product(
                productId,
                "Updated Organic T-Shirt",
                "Updated description",
                Money.ofPEN(34.99),
                CategoryId.generate(),
                StockQuantity.of(100),
                true,
                userId,
                LocalDateTime.now(),
                LocalDateTime.now(),
                userId,
                userId
        );

        when(productService.updateProduct(eq(productId), any())).thenReturn(updatedProduct);
        when(productMapper.toProductResponse(updatedProduct)).thenReturn(createProductResponse(updatedProduct));

        // Act & Assert
        mockMvc.perform(put("/api/v1/products/{id}", productId.getValue())
                                .header("X-User-Id", userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value("Updated Organic T-Shirt"))
               .andExpect(jsonPath("$.description").value("Updated description"))
               .andExpect(jsonPath("$.price").value(34.99));
    }

    @Test
    void updateStock_ShouldReturnUpdatedProduct() throws Exception {
        // Arrange
        UpdateStockRequest request = new UpdateStockRequest(50, userId.toString());

        Product updatedProduct = new Product(
                productId,
                "Organic Cotton T-Shirt",
                "Sustainable organic cotton t-shirt",
                Money.ofPEN(29.99),
                CategoryId.generate(),
                StockQuantity.of(50),
                true,
                userId,
                LocalDateTime.now(),
                LocalDateTime.now(),
                userId,
                userId
        );

        when(productService.updateStock(productId.getValue(), 50, userId)).thenReturn(updatedProduct);
        when(productMapper.toProductResponse(updatedProduct)).thenReturn(createProductResponse(updatedProduct));

        // Act & Assert
        mockMvc.perform(patch("/api/v1/products/{id}/stock", productId.getValue())
                                .header("X-User-Id", userId)
                                .param("quantity", "50"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.stockQuantity").value(50));
    }

    @Test
    void deleteProduct_ShouldReturnNoContent() throws Exception {
        // Arrange
        // deleteProduct is a void method, so we use doNothing()
        doNothing().when(productService).deleteProduct(productId.getValue());

        // Act & Assert
        mockMvc.perform(delete("/api/v1/products/{id}", productId.getValue()))
               .andExpect(status().isNoContent());
    }

    @Test
    void getProductsByCategory_ShouldReturnProducts() throws Exception {
        // Arrange
        UUID categoryId = UUID.randomUUID();
        Page<Product> productPage = new PageImpl<>(List.of(testProduct), PageRequest.of(0, 10), 10);
        when(productService.getProductsByCategory(any(), any(Pageable.class))).thenReturn(productPage);
        when(productMapper.toProductResponse(testProduct)).thenReturn(createProductResponse(testProduct));

        // Act & Assert
        mockMvc.perform(get("/api/v1/products/category/{categoryId}", categoryId)
                                .param("page", "0")
                                .param("size", "10"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.products[0].name").value("Organic Cotton T-Shirt"))
               .andExpect(jsonPath("$.totalElements").value(10));
    }

    @Test
    void searchProducts_ShouldReturnMatchingProducts() throws Exception {
        // Arrange
        Page<Product> productPage = new PageImpl<>(List.of(testProduct), PageRequest.of(0, 10), 10);
        when(productService.searchProductsByName(eq("organic"), any(Pageable.class))).thenReturn(productPage);
        when(productMapper.toProductResponse(testProduct)).thenReturn(createProductResponse(testProduct));

        // Act & Assert
        mockMvc.perform(get("/api/v1/products/search")
                                .param("q", "organic")
                                .param("page", "0")
                                .param("size", "10"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.products[0].name").value("Organic Cotton T-Shirt"))
               .andExpect(jsonPath("$.totalElements").value(10));
   }

    @Test
    void createProduct_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        // Arrange
        CreateProductRequest invalidRequest = new CreateProductRequest(
                "", // Invalid: empty name
                "Description",
                new BigDecimal("-10.00"), // Invalid: negative price
                UUID.randomUUID(),
                -5, // Invalid: negative stock
                userId
        );

        // Act & Assert
        mockMvc.perform(post("/api/v1/products")
                                .header("X-User-Id", userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidRequest)))
               .andExpect(status().isBadRequest());
   }

   private com.greenkawsay.catalog.infrastructure.adapters.in.web.dto.response.ProductResponse createProductResponse(Product product) {
       return new com.greenkawsay.catalog.infrastructure.adapters.in.web.dto.response.ProductResponse(
               product.getId().getValue(),
               product.getName(),
               product.getDescription(),
               product.getPrice().getAmount(),
               product.getCategoryId().getValue(),
               null, // categoryName will be set in controller
               product.getUserId(),
               product.getStockQuantity().getValue(),
               product.getCreatedAt(),
               product.getUpdatedAt()
       );
   }
}