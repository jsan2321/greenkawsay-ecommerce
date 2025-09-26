package com.greenkawsay.catalog.infrastructure.adapters.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Request DTO for updating an existing product
 */
@Schema(description = "Request payload for updating an existing product")
public class UpdateProductRequest {

    @NotBlank(message = "Product name is required")
    @Size(min = 1, max = 255, message = "Product name must be between 1 and 255 characters")
    @Schema(description = "Product name", example = "Organic Cotton T-Shirt - Updated", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Size(max = 1000, message = "Product description cannot exceed 1000 characters")
    @Schema(description = "Product description", example = "Made from 100% organic cotton, sustainably sourced - Updated description")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Price must have up to 10 integer digits and 2 decimal places")
    @Schema(description = "Product price", example = "34.99", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal price;

    @NotNull(message = "Category ID is required")
    @Schema(description = "Category ID", example = "123e4567-e89b-12d3-a456-426614174000", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID categoryId;

    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock quantity cannot be negative")
    @Max(value = 100000, message = "Stock quantity cannot exceed 100000")
    @Schema(description = "Stock quantity", example = "150", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer stockQuantity;

    @NotNull(message = "User ID is required")
    @Schema(description = "User ID of the user updating the product", example = "123e4567-e89b-12d3-a456-426614174000", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID userId;

    // Default constructor for JSON deserialization
    public UpdateProductRequest() {
    }

    public UpdateProductRequest(String name, String description, BigDecimal price, UUID categoryId, Integer stockQuantity, UUID userId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.categoryId = categoryId;
        this.stockQuantity = stockQuantity;
        this.userId = userId;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}