package com.greenkawsay.catalog.infrastructure.adapters.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Response DTO for product list operations with pagination support
 */
@Schema(description = "Paginated product list response")
public class ProductListResponse {

    @Schema(description = "List of products")
    private List<ProductResponse> products;

    @Schema(description = "Current page number", example = "1")
    private int currentPage;

    @Schema(description = "Total number of pages", example = "5")
    private int totalPages;

    @Schema(description = "Total number of products", example = "50")
    private long totalElements;

    @Schema(description = "Number of products per page", example = "10")
    private int pageSize;

    @Schema(description = "Indicates if this is the first page", example = "true")
    private boolean first;

    @Schema(description = "Indicates if this is the last page", example = "false")
    private boolean last;

    // Default constructor for JSON serialization
    public ProductListResponse() {
    }

    public ProductListResponse(List<ProductResponse> products, int currentPage, int totalPages, 
                              long totalElements, int pageSize, boolean first, boolean last) {
        this.products = products;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.pageSize = pageSize;
        this.first = first;
        this.last = last;
    }

    // Getters and setters
    public List<ProductResponse> getProducts() {
        return products;
    }

    public void setProducts(List<ProductResponse> products) {
        this.products = products;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }
}