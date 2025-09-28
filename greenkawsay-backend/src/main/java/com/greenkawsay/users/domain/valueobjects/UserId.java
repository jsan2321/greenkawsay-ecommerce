package com.greenkawsay.users.domain.valueobjects;

import com.greenkawsay.shared.domain.valueobjects.UUIDWrapper;

import java.util.UUID;

/**
 * Value object representing a User ID
 * Immutable and thread-safe
 */
public final class UserId extends UUIDWrapper {
    
    public UserId(UUID value) {
        super(value);
    }
    
    public UserId(String value) {
        super(value);
    }
    
    public static UserId generate() {
        return new UserId(UUID.randomUUID());
    }
    
    public static UserId fromString(String value) {
        return new UserId(value);
    }
}