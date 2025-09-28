package com.greenkawsay.users.infrastructure.adapters.in.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Response DTO for wishlist item operations
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WishlistItemResponse {
    private String id;
    private String wishlistId;
    private String productId;
    private LocalDateTime addedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}