package com.greenkawsay.catalog.infrastructure.adapters.in.web.mappers;

import com.greenkawsay.catalog.application.commands.CreateProductCommand;
import com.greenkawsay.catalog.application.commands.UpdateProductCommand;
import com.greenkawsay.catalog.application.commands.UpdateStockCommand;
import com.greenkawsay.catalog.domain.models.Product;
import com.greenkawsay.catalog.domain.valueobjects.CategoryId;
import com.greenkawsay.catalog.domain.valueobjects.ProductId;
import com.greenkawsay.catalog.domain.valueobjects.StockQuantity;
import com.greenkawsay.shared.domain.valueobjects.Money;
import com.greenkawsay.catalog.infrastructure.adapters.in.web.dto.request.CreateProductRequest;
import com.greenkawsay.catalog.infrastructure.adapters.in.web.dto.request.UpdateProductRequest;
import com.greenkawsay.catalog.infrastructure.adapters.in.web.dto.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * MapStruct mapper for Product-related DTOs
 */
@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    /**
     * Maps CreateProductRequest to CreateProductCommand
     */
    @Mapping(source = "request.price", target = "price", qualifiedByName = "bigDecimalToMoney")
    @Mapping(source = "request.categoryId", target = "categoryId", qualifiedByName = "uuidToCategoryId")
    @Mapping(source = "request.stockQuantity", target = "stockQuantity", qualifiedByName = "integerToStockQuantity")
    @Mapping(target = "isActive", constant = "true")
    CreateProductCommand toCreateProductCommand(CreateProductRequest request, UUID userId);

    /**
     * Maps UpdateProductRequest to UpdateProductCommand
     */
    @Mapping(source = "request.price", target = "price", qualifiedByName = "bigDecimalToMoney")
    @Mapping(source = "request.categoryId", target = "categoryId", qualifiedByName = "uuidToCategoryId")
    @Mapping(source = "request.stockQuantity", target = "stockQuantity", qualifiedByName = "integerToStockQuantity")
    @Mapping(target = "isActive", constant = "true")
    UpdateProductCommand toUpdateProductCommand(UpdateProductRequest request, UUID productId, UUID userId);

    /**
     * Maps Product domain model to ProductResponse
     */
    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "categoryId.value", target = "categoryId")
    @Mapping(source = "price.amount", target = "price")
    @Mapping(source = "stockQuantity.value", target = "stockQuantity")
    @Mapping(target = "categoryName", ignore = true) // Will be set in controller
    ProductResponse toProductResponse(Product product);

    /**
     * Maps list of Product domain models to list of ProductResponse
     */
    List<ProductResponse> toProductResponseList(List<Product> products);

    // Custom mapping methods
    @Named("bigDecimalToMoney")
    default Money bigDecimalToMoney(BigDecimal amount) {
        return amount != null ? new Money(amount, "USD") : null;
    }

    @Named("uuidToCategoryId")
    default CategoryId uuidToCategoryId(UUID categoryId) {
        return categoryId != null ? new CategoryId(categoryId) : null;
    }

    @Named("integerToStockQuantity")
    default StockQuantity integerToStockQuantity(Integer quantity) {
        return quantity != null ? new StockQuantity(quantity) : null;
    }

    @Named("uuidToProductId")
    default ProductId uuidToProductId(UUID productId) {
        return productId != null ? new ProductId(productId) : null;
    }
}