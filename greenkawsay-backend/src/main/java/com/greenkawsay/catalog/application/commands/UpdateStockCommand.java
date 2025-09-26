package com.greenkawsay.catalog.application.commands;

import com.greenkawsay.catalog.domain.valueobjects.StockQuantity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * Command DTO for updating product stock quantity
 */
public class UpdateStockCommand {
    
    @NotNull(message = "Stock quantity is required")
    @PositiveOrZero(message = "Stock quantity cannot be negative")
    private final StockQuantity stockQuantity;

    public UpdateStockCommand(StockQuantity stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    // Getters
    public StockQuantity getStockQuantity() {
        return stockQuantity;
    }
}