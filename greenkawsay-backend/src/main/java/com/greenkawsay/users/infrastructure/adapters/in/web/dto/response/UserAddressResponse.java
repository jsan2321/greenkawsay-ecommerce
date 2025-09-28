package com.greenkawsay.users.infrastructure.adapters.in.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Response DTO for user address operations
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAddressResponse {
    private String id;
    private String userId;
    private String street;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String apartmentNumber;
    private boolean isDefault;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}