package com.greenkawsay.catalog.domain.exceptions;

import com.greenkawsay.catalog.domain.valueobjects.CategoryId;
import com.greenkawsay.shared.domain.exceptions.DomainException;

/**
 * Exception thrown when a category is not found
 */
public class CategoryNotFoundException extends DomainException {
    
    public CategoryNotFoundException(CategoryId categoryId) {
        super(String.format("Category with ID '%s' not found", categoryId.getValue()), 
              "CATEGORY_NOT_FOUND");
    }
    
    public CategoryNotFoundException(String categorySlug) {
        super(String.format("Category with slug '%s' not found", categorySlug), 
              "CATEGORY_NOT_FOUND");
    }
}