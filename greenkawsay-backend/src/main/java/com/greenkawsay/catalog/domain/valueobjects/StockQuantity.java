package com.greenkawsay.catalog.domain.valueobjects;

import java.util.Objects;

/**
 * Value object representing product stock quantity with validation
 * Immutable and thread-safe
 */
public final class StockQuantity {
    private final int value;

    public StockQuantity(int value) {
        this.value = value;
        validate();
    }

    private void validate() {
        if (value < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }
        
        if (value > 1_000_000) {
            throw new IllegalArgumentException("Stock quantity cannot exceed 1,000,000");
        }
    }

    public int getValue() {
        return value;
    }

    public boolean isZero() {
        return value == 0;
    }

    public boolean isPositive() {
        return value > 0;
    }

    public boolean isGreaterThan(StockQuantity other) {
        return this.value > other.value;
    }

    public boolean isLessThan(StockQuantity other) {
        return this.value < other.value;
    }

    public StockQuantity add(StockQuantity other) {
        return new StockQuantity(this.value + other.value);
    }

    public StockQuantity add(int quantity) {
        return new StockQuantity(this.value + quantity);
    }

    public StockQuantity subtract(StockQuantity other) {
        return new StockQuantity(this.value - other.value);
    }

    public StockQuantity subtract(int quantity) {
        return new StockQuantity(this.value - quantity);
    }

    public static StockQuantity zero() {
        return new StockQuantity(0);
    }

    public static StockQuantity of(int value) {
        return new StockQuantity(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockQuantity that = (StockQuantity) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}