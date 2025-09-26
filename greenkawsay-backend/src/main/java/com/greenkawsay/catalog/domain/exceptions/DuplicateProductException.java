package com.greenkawsay.catalog.domain.exceptions;

import com.greenkawsay.shared.domain.exceptions.DomainException;

/**
 * Exception thrown when attempting to create a product with a duplicate name
 */
public class DuplicateProductException extends DomainException {
    
    public DuplicateProductException(String productName) {
        super(String.format("Product with name '%s' already exists", productName), 
              "DUPLICATE_PRODUCT");
    }
}