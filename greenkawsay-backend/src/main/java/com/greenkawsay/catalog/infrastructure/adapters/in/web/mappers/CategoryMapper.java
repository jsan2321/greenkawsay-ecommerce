package com.greenkawsay.catalog.infrastructure.adapters.in.web.mappers;

import com.greenkawsay.catalog.application.commands.CreateCategoryCommand;
import com.greenkawsay.catalog.application.commands.UpdateCategoryCommand;
import com.greenkawsay.catalog.domain.models.Category;
import com.greenkawsay.catalog.domain.valueobjects.CategoryId;
import com.greenkawsay.catalog.infrastructure.adapters.in.web.dto.request.CreateCategoryRequest;
import com.greenkawsay.catalog.infrastructure.adapters.in.web.dto.request.UpdateCategoryRequest;
import com.greenkawsay.catalog.infrastructure.adapters.in.web.dto.response.CategoryResponse;
import com.greenkawsay.catalog.infrastructure.adapters.in.web.dto.response.CategoryTreeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.UUID;

/**
 * MapStruct mapper for Category-related DTOs
 */
@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    /**
     * Maps CreateCategoryRequest to CreateCategoryCommand
     */
    @Mapping(source = "parentId", target = "parentId", qualifiedByName = "uuidToCategoryId")
    CreateCategoryCommand toCreateCategoryCommand(CreateCategoryRequest request);

    /**
     * Maps UpdateCategoryRequest to UpdateCategoryCommand
     */
    @Mapping(source = "parentId", target = "parentId", qualifiedByName = "uuidToCategoryId")
    UpdateCategoryCommand toUpdateCategoryCommand(UpdateCategoryRequest request);

    /**
     * Maps Category domain model to CategoryResponse
     */
    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "parentId.value", target = "parentId")
    @Mapping(target = "parentName", ignore = true) // Will be set in controller
    @Mapping(target = "productCount", ignore = true) // Will be set in controller
    @Mapping(target = "subcategoryCount", ignore = true) // Will be set in controller
    CategoryResponse toCategoryResponse(Category category);

    /**
     * Maps list of Category domain models to list of CategoryResponse
     */
    List<CategoryResponse> toCategoryResponseList(List<Category> categories);

    /**
     * Maps Category domain model to CategoryTreeResponse
     */
    @Mapping(source = "id.value", target = "id")
    @Mapping(target = "subcategories", ignore = true) // Will be set in controller
    CategoryTreeResponse toCategoryTreeResponse(Category category);

    /**
     * Maps list of Category domain models to list of CategoryTreeResponse
     */
    List<CategoryTreeResponse> toCategoryTreeResponseList(List<Category> categories);

    // Custom mapping methods
    @Named("uuidToCategoryId")
    default CategoryId uuidToCategoryId(UUID categoryId) {
        return categoryId != null ? new CategoryId(categoryId) : null;
    }
}