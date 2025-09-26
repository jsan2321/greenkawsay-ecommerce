package com.greenkawsay.shared.domain.valueobjects;

import java.util.Objects;
import java.util.UUID;

/**
 * Base class for UUID-based value objects
 * Provides common functionality for ID value objects across bounded contexts
 */
public abstract class UUIDWrapper {
    protected final UUID value;

    protected UUIDWrapper(UUID value) {
        this.value = Objects.requireNonNull(value, "UUID value cannot be null");
    }

    protected UUIDWrapper(String value) {
        this(UUID.fromString(Objects.requireNonNull(value, "UUID string cannot be null")));
    }

    public UUID getValue() {
        return value;
    }

    public String getValueAsString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UUIDWrapper that = (UUIDWrapper) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "value=" + value +
                '}';
    }

    /**
     * Generate a new random UUID for this wrapper type
     */
    public static <T extends UUIDWrapper> T generate(Class<T> clazz) {
        try {
            return clazz.getConstructor(UUID.class).newInstance(UUID.randomUUID());
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate UUID wrapper for " + clazz.getSimpleName(), e);
        }
    }
}