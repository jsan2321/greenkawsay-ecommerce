package com.greenkawsay.catalog.domain.models;

import com.greenkawsay.catalog.domain.valueobjects.CategoryId;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Domain model representing a product category
 * Contains business logic for category management and hierarchy
 */
public class Category {
    private final CategoryId id;
    private String name;
    private String slug;
    private String description;
    private CategoryId parentId;
    // Products will be managed through repository, not directly in domain model
    
    // Audit fields
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID createdBy;
    private UUID updatedBy;

    // Constructor for creating new category
    public Category(String name, String slug, String description, CategoryId parentId, UUID createdBy) {
        this.id = CategoryId.generate();
        this.name = Objects.requireNonNull(name, "Name cannot be null").trim();
        this.slug = Objects.requireNonNull(slug, "Slug cannot be null").trim().toLowerCase();
        this.description = description != null ? description.trim() : null;
        this.parentId = parentId;
        // Products will be managed through repository
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
        this.createdBy = Objects.requireNonNull(createdBy, "Created by cannot be null");
        this.updatedBy = this.createdBy;
        validate();
    }

    // Constructor for loading existing category
    public Category(CategoryId id, String name, String slug, String description, CategoryId parentId,
                   LocalDateTime createdAt, LocalDateTime updatedAt, UUID createdBy, UUID updatedBy) {
        this.id = Objects.requireNonNull(id, "ID cannot be null");
        this.name = Objects.requireNonNull(name, "Name cannot be null").trim();
        this.slug = Objects.requireNonNull(slug, "Slug cannot be null").trim().toLowerCase();
        this.description = description != null ? description.trim() : null;
        this.parentId = parentId;
        // Products will be managed through repository
        this.createdAt = Objects.requireNonNull(createdAt, "Created at cannot be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "Updated at cannot be null");
        this.createdBy = Objects.requireNonNull(createdBy, "Created by cannot be null");
        this.updatedBy = Objects.requireNonNull(updatedBy, "Updated by cannot be null");
        validate();
    }

    private void validate() {
        if (name.isBlank()) {
            throw new IllegalArgumentException("Category name cannot be empty");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("Category name cannot exceed 100 characters");
        }
        if (slug.isBlank()) {
            throw new IllegalArgumentException("Category slug cannot be empty");
        }
        if (slug.length() > 100) {
            throw new IllegalArgumentException("Category slug cannot exceed 100 characters");
        }
        if (description != null && description.length() > 1000) {
            throw new IllegalArgumentException("Category description cannot exceed 1000 characters");
        }
        // Prevent circular reference
        if (parentId != null && parentId.equals(id)) {
            throw new IllegalArgumentException("Category cannot be its own parent");
        }
    }

    // Business methods
    public void updateName(String name, UUID updatedBy) {
        this.name = Objects.requireNonNull(name, "Name cannot be null").trim();
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = Objects.requireNonNull(updatedBy, "Updated by cannot be null");
        validate();
    }

    public void updateDescription(String description, UUID updatedBy) {
        this.description = description != null ? description.trim() : null;
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = Objects.requireNonNull(updatedBy, "Updated by cannot be null");
        validate();
    }

    public void changeParent(CategoryId parentId, UUID updatedBy) {
        this.parentId = parentId;
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = Objects.requireNonNull(updatedBy, "Updated by cannot be null");
        validate();
    }

    public boolean hasParent() {
        return parentId != null;
    }

    // Product management will be handled through repository pattern
    // Products are not directly managed in the domain model to avoid
    // loading all products when working with categories

    public boolean isRootCategory() {
        return parentId == null;
    }

    // Getters
    public CategoryId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }

    public String getDescription() {
        return description;
    }

    public CategoryId getParentId() {
        return parentId;
    }

    // Products are managed through repository, not directly exposed

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public UUID getCreatedBy() {
        return createdBy;
    }

    public UUID getUpdatedBy() {
        return updatedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", slug='" + slug + '\'' +
                ", parentId=" + parentId +
                '}';
    }
}