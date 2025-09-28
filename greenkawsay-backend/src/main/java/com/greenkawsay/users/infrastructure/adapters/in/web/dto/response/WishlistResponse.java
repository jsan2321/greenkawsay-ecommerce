package com.greenkawsay.users.infrastructure.adapters.in.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Response DTO for wishlist operations
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WishlistResponse {
    private String id;
    private String userId;
    private String name;
    private String description;
    private boolean isPublic;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<WishlistItemResponse> items;
}