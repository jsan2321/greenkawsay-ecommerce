package com.greenkawsay.catalog.domain.valueobjects;

import com.greenkawsay.shared.domain.valueobjects.UUIDWrapper;

import java.util.UUID;

/**
 * Value object representing a Product identifier
 * Extends UUIDWrapper for common UUID functionality
 */
public final class ProductId extends UUIDWrapper {
    public ProductId(UUID value) {
        super(value);
    }

    public ProductId(String value) {
        super(value);
    }

    public static ProductId generate() {
        return new ProductId(UUID.randomUUID());
    }

    public static ProductId fromString(String value) {
        return new ProductId(value);
    }
}