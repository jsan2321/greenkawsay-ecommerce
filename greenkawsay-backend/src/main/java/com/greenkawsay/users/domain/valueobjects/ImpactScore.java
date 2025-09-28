package com.greenkawsay.users.domain.valueobjects;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Value object representing ecological impact score
 * Immutable and thread-safe
 */
public final class ImpactScore {
    private final BigDecimal value;
    
    public ImpactScore(BigDecimal value) {
        this.value = Objects.requireNonNull(value, "Impact score cannot be null");
        validate();
    }
    
    public ImpactScore(double value) {
        this(BigDecimal.valueOf(value));
    }
    
    public ImpactScore(String value) {
        this(new BigDecimal(Objects.requireNonNull(value, "Impact score string cannot be null")));
    }
    
    private void validate() {
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Impact score cannot be negative");
        }
        
        if (value.scale() > 2) {
            throw new IllegalArgumentException("Impact score cannot have more than 2 decimal places");
        }
        
        if (value.compareTo(new BigDecimal("999999.99")) > 0) {
            throw new IllegalArgumentException("Impact score cannot exceed 999999.99");
        }
    }
    
    public BigDecimal getValue() {
        return value;
    }
    
    public double getValueAsDouble() {
        return value.doubleValue();
    }
    
    public ImpactScore add(ImpactScore other) {
        return new ImpactScore(this.value.add(other.value));
    }
    
    public ImpactScore subtract(ImpactScore other) {
        BigDecimal result = this.value.subtract(other.value);
        if (result.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Impact score cannot become negative");
        }
        return new ImpactScore(result);
    }
    
    public ImpactScore multiply(BigDecimal multiplier) {
        return new ImpactScore(this.value.multiply(multiplier));
    }
    
    public boolean isGreaterThan(ImpactScore other) {
        return this.value.compareTo(other.value) > 0;
    }
    
    public boolean isLessThan(ImpactScore other) {
        return this.value.compareTo(other.value) < 0;
    }
    
    public boolean isZero() {
        return this.value.compareTo(BigDecimal.ZERO) == 0;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImpactScore that = (ImpactScore) o;
        return value.compareTo(that.value) == 0;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return value.toString();
    }
    
    public static ImpactScore zero() {
        return new ImpactScore(BigDecimal.ZERO);
    }
    
    public static ImpactScore of(double value) {
        return new ImpactScore(value);
    }
}