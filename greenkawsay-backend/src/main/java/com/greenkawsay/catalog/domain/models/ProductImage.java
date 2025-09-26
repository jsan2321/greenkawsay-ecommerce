package com.greenkawsay.catalog.domain.models;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Domain model representing a product image
 * Contains business logic for image management and primary image designation
 */
public class ProductImage {
    private final UUID id;
    private final UUID productId;
    private String imageUrl;
    private boolean isPrimary;
    
    // Audit fields
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID createdBy;
    private UUID updatedBy;

    // Constructor for creating new product image
    public ProductImage(UUID productId, String imageUrl, boolean isPrimary, UUID createdBy) {
        this.id = UUID.randomUUID();
        this.productId = Objects.requireNonNull(productId, "Product ID cannot be null");
        this.imageUrl = Objects.requireNonNull(imageUrl, "Image URL cannot be null").trim();
        this.isPrimary = isPrimary;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
        this.createdBy = Objects.requireNonNull(createdBy, "Created by cannot be null");
        this.updatedBy = this.createdBy;
        validate();
    }

    // Constructor for loading existing product image
    public ProductImage(UUID id, UUID productId, String imageUrl, boolean isPrimary,
                       LocalDateTime createdAt, LocalDateTime updatedAt, UUID createdBy, UUID updatedBy) {
        this.id = Objects.requireNonNull(id, "ID cannot be null");
        this.productId = Objects.requireNonNull(productId, "Product ID cannot be null");
        this.imageUrl = Objects.requireNonNull(imageUrl, "Image URL cannot be null").trim();
        this.isPrimary = isPrimary;
        this.createdAt = Objects.requireNonNull(createdAt, "Created at cannot be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "Updated at cannot be null");
        this.createdBy = Objects.requireNonNull(createdBy, "Created by cannot be null");
        this.updatedBy = Objects.requireNonNull(updatedBy, "Updated by cannot be null");
        validate();
    }

    private void validate() {
        if (imageUrl.isBlank()) {
            throw new IllegalArgumentException("Image URL cannot be empty");
        }
        if (imageUrl.length() > 500) {
            throw new IllegalArgumentException("Image URL cannot exceed 500 characters");
        }
        // Basic URL validation
        if (!imageUrl.matches("^https?://.+")) {
            throw new IllegalArgumentException("Image URL must be a valid HTTP/HTTPS URL");
        }
    }

    // Business methods
    public void updateImageUrl(String imageUrl, UUID updatedBy) {
        this.imageUrl = Objects.requireNonNull(imageUrl, "Image URL cannot be null").trim();
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = Objects.requireNonNull(updatedBy, "Updated by cannot be null");
        validate();
    }

    public void setAsPrimary(UUID updatedBy) {
        this.isPrimary = true;
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = Objects.requireNonNull(updatedBy, "Updated by cannot be null");
    }

    public void setAsSecondary(UUID updatedBy) {
        this.isPrimary = false;
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = Objects.requireNonNull(updatedBy, "Updated by cannot be null");
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public UUID getProductId() {
        return productId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

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
        ProductImage that = (ProductImage) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ProductImage{" +
                "id=" + id +
                ", productId=" + productId +
                ", imageUrl='" + imageUrl + '\'' +
                ", isPrimary=" + isPrimary +
                '}';
    }
}