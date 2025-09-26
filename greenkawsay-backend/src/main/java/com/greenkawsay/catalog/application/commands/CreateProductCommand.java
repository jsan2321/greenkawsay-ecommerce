package com.greenkawsay.catalog.application.commands;

import com.greenkawsay.catalog.domain.valueobjects.CategoryId;
import com.greenkawsay.catalog.domain.valueobjects.StockQuantity;
import com.greenkawsay.shared.domain.valueobjects.Money;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * Command DTO for creating a new product
 */
public class CreateProductCommand {
    
    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 255, message = "Product name must be between 2 and 255 characters")
    private final String name;
    
    @NotBlank(message = "Product description is required")
    @Size(min = 10, max = 1000, message = "Product description must be between 10 and 1000 characters")
    private final String description;
    
    @NotNull(message = "Product price is required")
    private final Money price;
    
    @NotNull(message = "Category ID is required")
    private final CategoryId categoryId;
    
    @NotNull(message = "Stock quantity is required")
    @PositiveOrZero(message = "Stock quantity cannot be negative")
    private final StockQuantity stockQuantity;
    
    @NotNull(message = "User ID is required")
    private final String userId;
    
    private final boolean isActive;

    public CreateProductCommand(String name, String description, Money price, 
                              CategoryId categoryId, StockQuantity stockQuantity, 
                              String userId, boolean isActive) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.categoryId = categoryId;
        this.stockQuantity = stockQuantity;
        this.userId = userId;
        this.isActive = isActive;
    }

    // Getters
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

    public String getUserId() {
        return userId;
    }

    public boolean isActive() {
        return isActive;
    }
}