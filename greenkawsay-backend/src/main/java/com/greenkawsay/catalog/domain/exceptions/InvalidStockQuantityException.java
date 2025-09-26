package com.greenkawsay.catalog.domain.exceptions;

import com.greenkawsay.shared.domain.exceptions.DomainException;

/**
 * Exception thrown when attempting to set an invalid stock quantity
 */
public class InvalidStockQuantityException extends DomainException {
    
    public InvalidStockQuantityException(String message) {
        super(message, "INVALID_STOCK_QUANTITY");
    }
    
    public InvalidStockQuantityException(int currentStock, int requestedStock) {
        super(String.format("Cannot reduce stock from %d to %d - insufficient quantity", 
                           currentStock, requestedStock), 
              "INVALID_STOCK_QUANTITY");
    }
}