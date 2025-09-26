package com.greenkawsay.catalog.infrastructure.adapters.in.web.controllers;

import com.greenkawsay.catalog.application.ports.in.ProductServicePort;
import com.greenkawsay.catalog.domain.valueobjects.ProductId;
import com.greenkawsay.catalog.infrastructure.adapters.in.web.dto.request.CreateProductRequest;
import com.greenkawsay.catalog.infrastructure.adapters.in.web.dto.request.UpdateProductRequest;
import com.greenkawsay.catalog.infrastructure.adapters.in.web.dto.response.ProductListResponse;
import com.greenkawsay.catalog.infrastructure.adapters.in.web.dto.response.ProductResponse;
import com.greenkawsay.catalog.infrastructure.adapters.in.web.mappers.ProductMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST Controller for Product operations
 * Exposes CRUD and business operations for products
 */
@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Products", description = "Product management API")
public class ProductController {

    private final ProductServicePort productService;
    private final ProductMapper productMapper;

    public ProductController(ProductServicePort productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @PostMapping
    @Operation(summary = "Create a new product", description = "Creates a new sustainable product with the provided details")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Product created successfully",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Category not found"),
        @ApiResponse(responseCode = "409", description = "Product with same name already exists")
    })
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody CreateProductRequest request,
            @Parameter(description = "User ID creating the product") @RequestHeader("X-User-Id") UUID userId) {
        
        var command = productMapper.toCreateProductCommand(request, userId);
        var product = productService.createProduct(command);
        var response = productMapper.toProductResponse(product);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Retrieves a product by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Product found",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "400", description = "Invalid product ID format")
    })
    public ResponseEntity<ProductResponse> getProductById(
            @Parameter(description = "Product ID") @PathVariable UUID id) {
        
        ProductId productId = ProductId.fromString(id.toString());
        var product = productService.getProductById(productId);
        var response = productMapper.toProductResponse(product);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all products", description = "Retrieves a paginated list of products with optional filtering")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Products retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ProductListResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    public ResponseEntity<ProductListResponse> getAllProducts(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String sortDirection,
            @Parameter(description = "Category ID filter") @RequestParam(required = false) UUID categoryId,
            @Parameter(description = "Search term") @RequestParam(required = false) String search) {
        
        Pageable pageable = PageRequest.of(page, size, 
            Sort.by(Sort.Direction.fromString(sortDirection), sortBy));
        
        Page<ProductResponse> productsPage;
        
        if (categoryId != null && search != null) {
            productsPage = productService.searchProductsByCategoryAndName(categoryId, search, pageable)
                .map(productMapper::toProductResponse);
        } else if (categoryId != null) {
            productsPage = productService.getProductsByCategory(categoryId, pageable)
                .map(productMapper::toProductResponse);
        } else if (search != null) {
            productsPage = productService.searchProductsByName(search, pageable)
                .map(productMapper::toProductResponse);
        } else {
            productsPage = productService.getAllProducts(pageable)
                .map(productMapper::toProductResponse);
        }
        
        var response = new ProductListResponse(
            productsPage.getContent(),
            productsPage.getNumber(),
            productsPage.getSize(),
            productsPage.getTotalElements(),
            productsPage.getTotalPages(),
            productsPage.isFirst(),
            productsPage.isLast()
        );
        
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update product", description = "Updates an existing product with the provided details")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Product updated successfully",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "409", description = "Product with same name already exists")
    })
    public ResponseEntity<ProductResponse> updateProduct(
            @Parameter(description = "Product ID") @PathVariable UUID id,
            @Valid @RequestBody UpdateProductRequest request,
            @Parameter(description = "User ID updating the product") @RequestHeader("X-User-Id") UUID userId) {
        
        ProductId productId = ProductId.fromString(id.toString());
        var command = productMapper.toUpdateProductCommand(request, id, userId);
        var product = productService.updateProduct(productId, command);
        var response = productMapper.toProductResponse(product);
        
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/stock")
    @Operation(summary = "Update product stock", description = "Updates the stock quantity of a product")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Stock updated successfully",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid stock quantity"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<ProductResponse> updateStock(
            @Parameter(description = "Product ID") @PathVariable UUID id,
            @Parameter(description = "New stock quantity") @RequestParam int quantity,
            @Parameter(description = "User ID updating the stock") @RequestHeader("X-User-Id") UUID userId) {
        
        var product = productService.updateStock(id, quantity, userId);
        var response = productMapper.toProductResponse(product);
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product", description = "Deletes a product by its ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "400", description = "Invalid product ID format")
    })
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "Product ID") @PathVariable UUID id) {
        
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Get products by category", description = "Retrieves products belonging to a specific category")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Products retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ProductListResponse.class))),
        @ApiResponse(responseCode = "404", description = "Category not found"),
        @ApiResponse(responseCode = "400", description = "Invalid category ID format")
    })
    public ResponseEntity<ProductListResponse> getProductsByCategory(
            @Parameter(description = "Category ID") @PathVariable UUID categoryId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("name"));
        var productsPage = productService.getProductsByCategory(categoryId, pageable)
            .map(productMapper::toProductResponse);
        
        var response = new ProductListResponse(
            productsPage.getContent(),
            productsPage.getNumber(),
            productsPage.getSize(),
            productsPage.getTotalElements(),
            productsPage.getTotalPages(),
            productsPage.isFirst(),
            productsPage.isLast()
        );
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @Operation(summary = "Search products", description = "Searches products by name with pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search completed successfully",
                    content = @Content(schema = @Schema(implementation = ProductListResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters")
    })
    public ResponseEntity<ProductListResponse> searchProducts(
            @Parameter(description = "Search term") @RequestParam String q,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("name"));
        var productsPage = productService.searchProductsByName(q, pageable)
            .map(productMapper::toProductResponse);
        
        var response = new ProductListResponse(
            productsPage.getContent(),
            productsPage.getNumber(),
            productsPage.getSize(),
            productsPage.getTotalElements(),
            productsPage.getTotalPages(),
            productsPage.isFirst(),
            productsPage.isLast()
        );
        
        return ResponseEntity.ok(response);
    }
}