package com.greenkawsay.users.domain.valueobjects;

import com.greenkawsay.shared.domain.valueobjects.UUIDWrapper;

import java.util.UUID;

/**
 * Value object representing an Address ID
 * Immutable and thread-safe
 */
public final class AddressId extends UUIDWrapper {
    
    public AddressId(UUID value) {
        super(value);
    }
    
    public AddressId(String value) {
        super(value);
    }
    
    public static AddressId generate() {
        return new AddressId(UUID.randomUUID());
    }
    
    public static AddressId fromString(String value) {
        return new AddressId(value);
    }
}