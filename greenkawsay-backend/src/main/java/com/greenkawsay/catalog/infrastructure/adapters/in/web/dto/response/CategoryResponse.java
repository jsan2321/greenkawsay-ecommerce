package com.greenkawsay.catalog.infrastructure.adapters.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for category operations
 */
@Schema(description = "Category response data")
public class CategoryResponse {

    @Schema(description = "Category ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "Category name", example = "Organic Foods")
    private String name;

    @Schema(description = "Category slug (URL-friendly identifier)", example = "organic-foods")
    private String slug;

    @Schema(description = "Category description", example = "Products made from organic and sustainable sources")
    private String description;

    @Schema(description = "Parent category ID (null for root categories)", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID parentId;

    @Schema(description = "Parent category name (null for root categories)", example = "Food & Beverages")
    private String parentName;

    @Schema(description = "Number of products in this category", example = "25")
    private int productCount;

    @Schema(description = "Number of subcategories", example = "3")
    private int subcategoryCount;

    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;

    // Default constructor for JSON serialization
    public CategoryResponse() {
    }

    public CategoryResponse(UUID id, String name, String slug, String description, UUID parentId, 
                           String parentName, int productCount, int subcategoryCount, 
                           LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.parentId = parentId;
        this.parentName = parentName;
        this.productCount = productCount;
        this.subcategoryCount = subcategoryCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    public int getSubcategoryCount() {
        return subcategoryCount;
    }

    public void setSubcategoryCount(int subcategoryCount) {
        this.subcategoryCount = subcategoryCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}