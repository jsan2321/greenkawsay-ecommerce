package com.greenkawsay.catalog.domain.models;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Domain model representing a product review
 * Contains business logic for rating validation and review management
 */
public class Review {
    private final UUID id;
    private final UUID productId;
    private final UUID userId; // Cross-context reference to Users context
    private int rating;
    private String comment;
    
    // Audit fields
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID createdBy;
    private UUID updatedBy;

    // Constructor for creating new review
    public Review(UUID productId, UUID userId, int rating, String comment, UUID createdBy) {
        this.id = UUID.randomUUID();
        this.productId = Objects.requireNonNull(productId, "Product ID cannot be null");
        this.userId = Objects.requireNonNull(userId, "User ID cannot be null");
        this.rating = rating;
        this.comment = comment != null ? comment.trim() : null;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
        this.createdBy = Objects.requireNonNull(createdBy, "Created by cannot be null");
        this.updatedBy = this.createdBy;
        validate();
    }

    // Constructor for loading existing review
    public Review(UUID id, UUID productId, UUID userId, int rating, String comment,
                  LocalDateTime createdAt, LocalDateTime updatedAt, UUID createdBy, UUID updatedBy) {
        this.id = Objects.requireNonNull(id, "ID cannot be null");
        this.productId = Objects.requireNonNull(productId, "Product ID cannot be null");
        this.userId = Objects.requireNonNull(userId, "User ID cannot be null");
        this.rating = rating;
        this.comment = comment != null ? comment.trim() : null;
        this.createdAt = Objects.requireNonNull(createdAt, "Created at cannot be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "Updated at cannot be null");
        this.createdBy = Objects.requireNonNull(createdBy, "Created by cannot be null");
        this.updatedBy = Objects.requireNonNull(updatedBy, "Updated by cannot be null");
        validate();
    }

    private void validate() {
        validateRating();
        if (comment != null && comment.length() > 1000) {
            throw new IllegalArgumentException("Review comment cannot exceed 1000 characters");
        }
    }

    private void validateRating() {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
    }

    // Business methods
    public void updateRating(int rating, UUID updatedBy) {
        this.rating = rating;
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = Objects.requireNonNull(updatedBy, "Updated by cannot be null");
        validateRating();
    }

    public void updateComment(String comment, UUID updatedBy) {
        this.comment = comment != null ? comment.trim() : null;
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = Objects.requireNonNull(updatedBy, "Updated by cannot be null");
        validate();
    }

    public boolean hasComment() {
        return comment != null && !comment.isBlank();
    }

    public boolean isPositiveReview() {
        return rating >= 4;
    }

    public boolean isNegativeReview() {
        return rating <= 2;
    }

    public boolean isNeutralReview() {
        return rating == 3;
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public UUID getProductId() {
        return productId;
    }

    public UUID getUserId() {
        return userId;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
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
        Review review = (Review) o;
        return Objects.equals(id, review.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", productId=" + productId +
                ", userId=" + userId +
                ", rating=" + rating +
                ", hasComment=" + hasComment() +
                '}';
    }
}