package com.greenkawsay.users.domain.exceptions;

import com.greenkawsay.shared.domain.exceptions.DomainException;
import com.greenkawsay.users.domain.valueobjects.WishlistId;

/**
 * Exception thrown when a wishlist is not found
 */
public class WishlistNotFoundException extends DomainException {
    
    public WishlistNotFoundException(WishlistId wishlistId) {
        super("Wishlist not found with ID: " + wishlistId.getValueAsString(), "WISHLIST_NOT_FOUND");
    }
    
    public WishlistNotFoundException(String message, Throwable cause) {
        super(message, "WISHLIST_NOT_FOUND", cause);
    }
}