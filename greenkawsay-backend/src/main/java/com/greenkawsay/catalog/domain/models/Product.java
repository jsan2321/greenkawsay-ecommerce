package com.greenkawsay.catalog.domain.models;

import com.greenkawsay.catalog.domain.valueobjects.ProductId;
import com.greenkawsay.catalog.domain.valueobjects.CategoryId;
import com.greenkawsay.catalog.domain.valueobjects.StockQuantity;
import com.greenkawsay.shared.domain.valueobjects.Money;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Domain model representing a product in the catalog
 * Contains business logic for product management, stock control, and pricing
 */
public class Product {
    private final ProductId id;
    private String name;
    private String description;
    private Money price;
    private CategoryId categoryId;
    private StockQuantity stockQuantity;
    private boolean isActive;
    
    // Cross-context reference (Users context)
    private final UUID userId;
    
    // Audit fields
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID createdBy;
    private UUID updatedBy;

    // Constructor for creating new product
    public Product(String name, String description, Money price, CategoryId categoryId, 
                   StockQuantity stockQuantity, UUID userId, UUID createdBy) {
        this.id = ProductId.generate();
        this.name = Objects.requireNonNull(name, "Name cannot be null").trim();
        this.description = description != null ? description.trim() : null;
        this.price = Objects.requireNonNull(price, "Price cannot be null");
        this.categoryId = Objects.requireNonNull(categoryId, "Category ID cannot be null");
        this.stockQuantity = Objects.requireNonNull(stockQuantity, "Stock quantity cannot be null");
        this.userId = Objects.requireNonNull(userId, "User ID cannot be null");
        this.isActive = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
        this.createdBy = Objects.requireNonNull(createdBy, "Created by cannot be null");
        this.updatedBy = this.createdBy;
        validate();
    }

    // Constructor for loading existing product
    public Product(ProductId id, String name, String description, Money price, CategoryId categoryId,
                   StockQuantity stockQuantity, boolean isActive, UUID userId,
                   LocalDateTime createdAt, LocalDateTime updatedAt, UUID createdBy, UUID updatedBy) {
        this.id = Objects.requireNonNull(id, "ID cannot be null");
        this.name = Objects.requireNonNull(name, "Name cannot be null").trim();
        this.description = description != null ? description.trim() : null;
        this.price = Objects.requireNonNull(price, "Price cannot be null");
        this.categoryId = Objects.requireNonNull(categoryId, "Category ID cannot be null");
        this.stockQuantity = Objects.requireNonNull(stockQuantity, "Stock quantity cannot be null");
        this.isActive = isActive;
        this.userId = Objects.requireNonNull(userId, "User ID cannot be null");
        this.createdAt = Objects.requireNonNull(createdAt, "Created at cannot be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "Updated at cannot be null");
        this.createdBy = Objects.requireNonNull(createdBy, "Created by cannot be null");
        this.updatedBy = Objects.requireNonNull(updatedBy, "Updated by cannot be null");
        validate();
    }

    private void validate() {
        if (name.isBlank()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        if (name.length() > 255) {
            throw new IllegalArgumentException("Product name cannot exceed 255 characters");
        }
        if (description != null && description.length() > 1000) {
            throw new IllegalArgumentException("Product description cannot exceed 1000 characters");
        }
        if (price.isZero()) {
            throw new IllegalArgumentException("Product price cannot be zero");
        }
        if (price.isLessThan(Money.zero(price.getCurrencyCode()))) {
            throw new IllegalArgumentException("Product price cannot be negative");
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

    public void updatePrice(Money price, UUID updatedBy) {
        this.price = Objects.requireNonNull(price, "Price cannot be null");
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = Objects.requireNonNull(updatedBy, "Updated by cannot be null");
        validate();
    }

    public void changeCategory(CategoryId categoryId, UUID updatedBy) {
        this.categoryId = Objects.requireNonNull(categoryId, "Category ID cannot be null");
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = Objects.requireNonNull(updatedBy, "Updated by cannot be null");
    }

    public void updateStock(StockQuantity stockQuantity, UUID updatedBy) {
        this.stockQuantity = Objects.requireNonNull(stockQuantity, "Stock quantity cannot be null");
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = Objects.requireNonNull(updatedBy, "Updated by cannot be null");
    }

    public void increaseStock(int quantity, UUID updatedBy) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        this.stockQuantity = this.stockQuantity.add(quantity);
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = Objects.requireNonNull(updatedBy, "Updated by cannot be null");
    }

    public void decreaseStock(int quantity, UUID updatedBy) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (stockQuantity.getValue() < quantity) {
            throw new IllegalArgumentException("Insufficient stock");
        }
        this.stockQuantity = this.stockQuantity.subtract(quantity);
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = Objects.requireNonNull(updatedBy, "Updated by cannot be null");
    }

    public void activate(UUID updatedBy) {
        this.isActive = true;
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = Objects.requireNonNull(updatedBy, "Updated by cannot be null");
    }

    public void deactivate(UUID updatedBy) {
        this.isActive = false;
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = Objects.requireNonNull(updatedBy, "Updated by cannot be null");
    }

    public boolean isAvailable() {
        return isActive && stockQuantity.isPositive();
    }

    public boolean isOutOfStock() {
        return stockQuantity.isZero();
    }

    public boolean hasSufficientStock(int quantity) {
        return stockQuantity.getValue() >= quantity;
    }

    // Getters
    public ProductId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Money getPrice() {
        return price;
    }

    public CategoryId getCategoryId() {
        return categoryId;
    }

    public StockQuantity getStockQuantity() {
        return stockQuantity;
    }

    public boolean isActive() {
        return isActive;
    }

    public UUID getUserId() {
        return userId;
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
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", categoryId=" + categoryId +
                ", isActive=" + isActive +
                '}';
    }
}