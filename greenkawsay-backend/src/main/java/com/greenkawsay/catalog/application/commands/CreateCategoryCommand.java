package com.greenkawsay.catalog.application.commands;

import com.greenkawsay.catalog.domain.valueobjects.CategoryId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Command DTO for creating a new category
 */
public class CreateCategoryCommand {
    
    @NotBlank(message = "Category name is required")
    @Size(min = 2, max = 100, message = "Category name must be between 2 and 100 characters")
    private final String name;
    
    @Size(max = 500, message = "Category description cannot exceed 500 characters")
    private final String description;
    
    private final CategoryId parentId;

    public CreateCategoryCommand(String name, String description, CategoryId parentId) {
        this.name = name;
        this.description = description;
        this.parentId = parentId;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public CategoryId getParentId() {
        return parentId;
    }
}