package com.greenkawsay.catalog.domain.exceptions;

import com.greenkawsay.catalog.domain.valueobjects.ProductId;
import com.greenkawsay.shared.domain.exceptions.DomainException;

/**
 * Exception thrown when a product is not found
 */
public class ProductNotFoundException extends DomainException {
    
    public ProductNotFoundException(ProductId productId) {
        super(String.format("Product with ID '%s' not found", productId.getValue()), 
              "PRODUCT_NOT_FOUND");
    }
    
    public ProductNotFoundException(String productName) {
        super(String.format("Product with name '%s' not found", productName), 
              "PRODUCT_NOT_FOUND");
    }
}