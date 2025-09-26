package com.greenkawsay.catalog.infrastructure.adapters.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for updating product stock quantity
 */
@Schema(description = "Request payload for updating product stock quantity")
public class UpdateStockRequest {

    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock quantity cannot be negative")
    @Max(value = 100000, message = "Stock quantity cannot exceed 100000")
    @Schema(description = "New stock quantity", example = "50", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer stockQuantity;

    @NotNull(message = "User ID is required")
    @Schema(description = "User ID of the user updating the stock", example = "123e4567-e89b-12d3-a456-426614174000", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userId;

    // Default constructor for JSON deserialization
    public UpdateStockRequest() {
    }

    public UpdateStockRequest(Integer stockQuantity, String userId) {
        this.stockQuantity = stockQuantity;
        this.userId = userId;
    }

    // Getters and setters
    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}