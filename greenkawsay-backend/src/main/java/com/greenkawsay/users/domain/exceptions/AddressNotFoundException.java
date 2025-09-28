package com.greenkawsay.users.domain.exceptions;

import com.greenkawsay.shared.domain.exceptions.DomainException;
import com.greenkawsay.users.domain.valueobjects.AddressId;

/**
 * Exception thrown when an address is not found
 */
public class AddressNotFoundException extends DomainException {
    
    public AddressNotFoundException(AddressId addressId) {
        super("Address not found with ID: " + addressId.getValueAsString(), "ADDRESS_NOT_FOUND");
    }
    
    public AddressNotFoundException(String message, Throwable cause) {
        super(message, "ADDRESS_NOT_FOUND", cause);
    }
}