package com.greenkawsay.catalog.application.services;

import com.greenkawsay.catalog.application.commands.CreateProductCommand;
import com.greenkawsay.catalog.application.commands.UpdateProductCommand;
import com.greenkawsay.catalog.application.commands.UpdateStockCommand;
import com.greenkawsay.catalog.application.ports.in.ProductServicePort;
import com.greenkawsay.catalog.domain.exceptions.CategoryNotFoundException;
import com.greenkawsay.catalog.domain.exceptions.DuplicateProductException;
import com.greenkawsay.catalog.domain.exceptions.InvalidStockQuantityException;
import com.greenkawsay.catalog.domain.exceptions.ProductNotFoundException;
import com.greenkawsay.catalog.domain.models.Product;
import com.greenkawsay.catalog.domain.repositories.CategoryRepository;
import com.greenkawsay.catalog.domain.repositories.ProductRepository;
import com.greenkawsay.catalog.domain.valueobjects.CategoryId;
import com.greenkawsay.catalog.domain.valueobjects.ProductId;
import com.greenkawsay.shared.domain.valueobjects.Money;
import com.greenkawsay.catalog.domain.valueobjects.StockQuantity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Application service for product use cases
 * Implements the ProductServicePort interface
 */
@Service
@Transactional
public class ProductApplicationService implements ProductServicePort {
    
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    
    public ProductApplicationService(ProductRepository productRepository, 
                                   CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }
    
    @Override
    public Product createProduct(CreateProductCommand command) {
        // Validate business rules
        validateProductCreation(command);
        
        // Create the product domain entity
        Product product = new Product(
            command.getName(),
            command.getDescription(),
            command.getPrice(),
            command.getCategoryId(),
            command.getStockQuantity(),
            java.util.UUID.fromString(command.getUserId()),
            java.util.UUID.fromString(command.getUserId())
        );
        
        // Save and return
        return productRepository.save(product);
    }
    
    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
    
    @Override
    public Page<Product> getProductsByCategory(UUID categoryId, Pageable pageable) {
        CategoryId catId = CategoryId.fromString(categoryId.toString());
        return productRepository.findByCategoryId(catId, pageable);
    }
    
    @Override
    public Page<Product> searchProductsByName(String searchTerm, Pageable pageable) {
        return productRepository.findByNameContaining(searchTerm, pageable);
    }
    
    @Override
    public Page<Product> searchProductsByCategoryAndName(UUID categoryId, String searchTerm, Pageable pageable) {
        CategoryId catId = CategoryId.fromString(categoryId.toString());
        return productRepository.findByCategoryIdAndNameContaining(catId, searchTerm, pageable);
    }
    
    @Override
    public Product updateStock(UUID productId, int quantity, UUID userId) {
        ProductId prodId = ProductId.fromString(productId.toString());
        Product product = productRepository.findById(prodId)
            .orElseThrow(() -> new ProductNotFoundException(prodId));
        
        StockQuantity stockQuantity = new StockQuantity(quantity);
        product.updateStock(stockQuantity, userId);
        
        return productRepository.save(product);
    }
    
    @Override
    public void deleteProduct(UUID productId) {
        ProductId prodId = ProductId.fromString(productId.toString());
        if (!productRepository.findById(prodId).isPresent()) {
            throw new ProductNotFoundException(prodId);
        }
        productRepository.deleteById(prodId);
    }
    
    
    @Override
    public Product getProductById(ProductId productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException(productId));
    }
    
    @Override
    public Product updateProduct(ProductId productId, UpdateProductCommand command) {
        // Find the product
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException(productId));
        
        // Validate category exists
        if (!categoryRepository.findById(command.getCategoryId()).isPresent()) {
            throw new CategoryNotFoundException(command.getCategoryId());
        }
        
        // Update product information
        product.updateName(command.getName(), product.getUserId());
        product.updateDescription(command.getDescription(), product.getUserId());
        product.updatePrice(command.getPrice(), product.getUserId());
        product.changeCategory(command.getCategoryId(), product.getUserId());
        product.updateStock(command.getStockQuantity(), product.getUserId());
        
        // Update active status
        if (command.isActive()) {
            product.activate(product.getUserId());
        } else {
            product.deactivate(product.getUserId());
        }
        
        // Save and return
        return productRepository.save(product);
    }
    
    
    @Override
    public List<Product> getAllActiveProducts() {
        return productRepository.findActiveProducts();
    }
    
    @Override
    public List<Product> getProductsByCategory(String categoryId) {
        CategoryId catId = CategoryId.fromString(categoryId);
        return productRepository.findActiveProductsByCategory(catId);
    }
    
    /**
     * Validate product creation business rules
     */
    private void validateProductCreation(CreateProductCommand command) {
        // Check if category exists
        if (!categoryRepository.findById(command.getCategoryId()).isPresent()) {
            throw new CategoryNotFoundException(command.getCategoryId());
        }
        
        // Check for duplicate product name
        if (productRepository.existsByName(command.getName())) {
            throw new DuplicateProductException(command.getName());
        }
        
        // Validate stock quantity (domain entity will handle this, but we can add additional validation here)
        if (command.getStockQuantity().getValue() < 0) {
            throw new InvalidStockQuantityException("Stock quantity cannot be negative");
        }
    }
}