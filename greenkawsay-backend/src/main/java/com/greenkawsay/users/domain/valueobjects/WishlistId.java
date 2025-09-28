package com.greenkawsay.users.domain.valueobjects;

import com.greenkawsay.shared.domain.valueobjects.UUIDWrapper;

import java.util.UUID;

/**
 * Value object representing a Wishlist ID
 * Immutable and thread-safe
 */
public final class WishlistId extends UUIDWrapper {
    
    public WishlistId(UUID value) {
        super(value);
    }
    
    public WishlistId(String value) {
        super(value);
    }
    
    public static WishlistId generate() {
        return new WishlistId(UUID.randomUUID());
    }
    
    public static WishlistId fromString(String value) {
        return new WishlistId(value);
    }
}