package com.greenkawsay.catalog.infrastructure.adapters.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Response DTO for hierarchical category tree structure
 */
@Schema(description = "Hierarchical category tree response")
public class CategoryTreeResponse {

    @Schema(description = "Category ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "Category name", example = "Organic Foods")
    private String name;

    @Schema(description = "Category slug (URL-friendly identifier)", example = "organic-foods")
    private String slug;

    @Schema(description = "Category description", example = "Products made from organic and sustainable sources")
    private String description;

    @Schema(description = "Number of products in this category", example = "25")
    private int productCount;

    @Schema(description = "List of subcategories (nested hierarchy)")
    private List<CategoryTreeResponse> subcategories;

    // Default constructor for JSON serialization
    public CategoryTreeResponse() {
        this.subcategories = new ArrayList<>();
    }

    public CategoryTreeResponse(UUID id, String name, String slug, String description, int productCount) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.productCount = productCount;
        this.subcategories = new ArrayList<>();
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

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    public List<CategoryTreeResponse> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<CategoryTreeResponse> subcategories) {
        this.subcategories = subcategories;
    }

    // Helper method to add a subcategory
    public void addSubcategory(CategoryTreeResponse subcategory) {
        if (this.subcategories == null) {
            this.subcategories = new ArrayList<>();
        }
        this.subcategories.add(subcategory);
    }

    // Helper method to check if category has subcategories
    public boolean hasSubcategories() {
        return subcategories != null && !subcategories.isEmpty();
    }
}