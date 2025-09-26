package com.greenkawsay.shared.domain.exceptions;

/**
 * Base class for all domain-specific exceptions
 */
public abstract class DomainException extends RuntimeException {
    
    private final String errorCode;
    
    public DomainException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public DomainException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
}