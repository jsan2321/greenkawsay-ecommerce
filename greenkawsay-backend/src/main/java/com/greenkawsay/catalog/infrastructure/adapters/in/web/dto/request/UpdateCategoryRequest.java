package com.greenkawsay.catalog.infrastructure.adapters.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.UUID;

/**
 * Request DTO for updating an existing category
 */
@Schema(description = "Request payload for updating an existing category")
public class UpdateCategoryRequest {

    @NotBlank(message = "Category name is required")
    @Size(min = 1, max = 100, message = "Category name must be between 1 and 100 characters")
    @Schema(description = "Category name", example = "Organic Foods - Updated", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Size(max = 500, message = "Category description cannot exceed 500 characters")
    @Schema(description = "Category description", example = "Products made from organic and sustainable sources - Updated description")
    private String description;

    @Schema(description = "Parent category ID (optional, for hierarchical categories)", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID parentId;

    // Default constructor for JSON deserialization
    public UpdateCategoryRequest() {
    }

    public UpdateCategoryRequest(String name, String description, UUID parentId) {
        this.name = name;
        this.description = description;
        this.parentId = parentId;
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

    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }
}