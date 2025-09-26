package com.greenkawsay.catalog.domain.valueobjects;

import com.greenkawsay.shared.domain.valueobjects.UUIDWrapper;

import java.util.UUID;

/**
 * Value object representing a Category identifier
 * Extends UUIDWrapper for common UUID functionality
 */
public final class CategoryId extends UUIDWrapper {
    public CategoryId(UUID value) {
        super(value);
    }

    public CategoryId(String value) {
        super(value);
    }

    public static CategoryId generate() {
        return new CategoryId(UUID.randomUUID());
    }

    public static CategoryId fromString(String value) {
        return new CategoryId(value);
    }
}