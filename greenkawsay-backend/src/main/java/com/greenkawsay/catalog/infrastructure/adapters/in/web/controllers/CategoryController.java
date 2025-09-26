package com.greenkawsay.catalog.infrastructure.adapters.in.web.controllers;

import com.greenkawsay.catalog.application.ports.in.CategoryServicePort;
import com.greenkawsay.catalog.domain.valueobjects.CategoryId;
import com.greenkawsay.catalog.infrastructure.adapters.in.web.dto.request.CreateCategoryRequest;
import com.greenkawsay.catalog.infrastructure.adapters.in.web.dto.request.UpdateCategoryRequest;
import com.greenkawsay.catalog.infrastructure.adapters.in.web.dto.response.CategoryResponse;
import com.greenkawsay.catalog.infrastructure.adapters.in.web.dto.response.CategoryTreeResponse;
import com.greenkawsay.catalog.infrastructure.adapters.in.web.mappers.CategoryMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller for Category operations
 * Exposes CRUD and hierarchical operations for categories
 */
@RestController
@RequestMapping("/api/v1/categories")
@Tag(name = "Categories", description = "Category management API")
public class CategoryController {

    private final CategoryServicePort categoryService;
    private final CategoryMapper categoryMapper;

    public CategoryController(CategoryServicePort categoryService, CategoryMapper categoryMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }

    @PostMapping
    @Operation(summary = "Create a new category", description = "Creates a new ecological category with optional parent for hierarchical structure")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Category created successfully",
                    content = @Content(schema = @Schema(implementation = CategoryResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Parent category not found"),
        @ApiResponse(responseCode = "409", description = "Category with same name already exists")
    })
    public ResponseEntity<CategoryResponse> createCategory(
            @Valid @RequestBody CreateCategoryRequest request) {
        
        var command = categoryMapper.toCreateCategoryCommand(request);
        var category = categoryService.createCategory(command.getName(), command.getParentId());
        var response = categoryMapper.toCategoryResponse(category);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by ID", description = "Retrieves a category by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Category found",
                    content = @Content(schema = @Schema(implementation = CategoryResponse.class))),
        @ApiResponse(responseCode = "404", description = "Category not found"),
        @ApiResponse(responseCode = "400", description = "Invalid category ID format")
    })
    public ResponseEntity<CategoryResponse> getCategoryById(
            @Parameter(description = "Category ID") @PathVariable UUID id) {
        
        CategoryId categoryId = CategoryId.fromString(id.toString());
        var category = categoryService.getCategoryById(categoryId);
        var response = categoryMapper.toCategoryResponse(category);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/slug/{slug}")
    @Operation(summary = "Get category by slug", description = "Retrieves a category by its URL-friendly slug")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Category found",
                    content = @Content(schema = @Schema(implementation = CategoryResponse.class))),
        @ApiResponse(responseCode = "404", description = "Category not found"),
        @ApiResponse(responseCode = "400", description = "Invalid slug format")
    })
    public ResponseEntity<CategoryResponse> getCategoryBySlug(
            @Parameter(description = "Category slug") @PathVariable String slug) {
        
        var category = categoryService.getCategoryBySlug(slug);
        var response = categoryMapper.toCategoryResponse(category);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tree")
    @Operation(summary = "Get category tree", description = "Retrieves the complete hierarchical category structure")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Category tree retrieved successfully",
                    content = @Content(schema = @Schema(implementation = CategoryTreeResponse.class)))
    })
    public ResponseEntity<List<CategoryTreeResponse>> getCategoryTree() {
        
        var categoryTree = categoryService.getCategoryTree();
        var response = categoryMapper.toCategoryTreeResponseList(categoryTree);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all active categories", description = "Retrieves all active categories in a flat list")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Categories retrieved successfully",
                    content = @Content(schema = @Schema(implementation = CategoryResponse.class)))
    })
    public ResponseEntity<List<CategoryResponse>> getAllActiveCategories() {
        
        var categories = categoryService.getAllActiveCategories();
        var response = categoryMapper.toCategoryResponseList(categories);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/roots")
    @Operation(summary = "Get root categories", description = "Retrieves all root categories (categories without parent)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Root categories retrieved successfully",
                    content = @Content(schema = @Schema(implementation = CategoryResponse.class)))
    })
    public ResponseEntity<List<CategoryResponse>> getRootCategories() {
        
        var categories = categoryService.getRootCategories();
        var response = categoryMapper.toCategoryResponseList(categories);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/subcategories")
    @Operation(summary = "Get subcategories", description = "Retrieves all subcategories of a parent category")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Subcategories retrieved successfully",
                    content = @Content(schema = @Schema(implementation = CategoryResponse.class))),
        @ApiResponse(responseCode = "404", description = "Parent category not found"),
        @ApiResponse(responseCode = "400", description = "Invalid category ID format")
    })
    public ResponseEntity<List<CategoryResponse>> getSubcategories(
            @Parameter(description = "Parent category ID") @PathVariable UUID id) {
        
        CategoryId parentId = CategoryId.fromString(id.toString());
        var subcategories = categoryService.getSubcategories(parentId);
        var response = categoryMapper.toCategoryResponseList(subcategories);
        
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update category", description = "Updates an existing category with the provided details")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Category updated successfully",
                    content = @Content(schema = @Schema(implementation = CategoryResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Category not found"),
        @ApiResponse(responseCode = "409", description = "Category with same name already exists")
    })
    public ResponseEntity<CategoryResponse> updateCategory(
            @Parameter(description = "Category ID") @PathVariable UUID id,
            @Valid @RequestBody UpdateCategoryRequest request) {
        
        CategoryId categoryId = CategoryId.fromString(id.toString());
        var command = categoryMapper.toUpdateCategoryCommand(request);
        var category = categoryService.updateCategory(categoryId, command.getName(), command.getParentId());
        var response = categoryMapper.toCategoryResponse(category);
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete category", description = "Deletes a category by its ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Category not found"),
        @ApiResponse(responseCode = "409", description = "Category has subcategories or products"),
        @ApiResponse(responseCode = "400", description = "Invalid category ID format")
    })
    public ResponseEntity<Void> deleteCategory(
            @Parameter(description = "Category ID") @PathVariable UUID id) {
        
        CategoryId categoryId = CategoryId.fromString(id.toString());
        categoryService.deleteCategory(categoryId);
        
        return ResponseEntity.noContent().build();
    }
}