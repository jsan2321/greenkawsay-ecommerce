package com.greenkawsay.users.infrastructure.adapters.in.web.controllers;

import com.greenkawsay.users.application.ports.in.UserProfileServicePort;
import com.greenkawsay.users.domain.exceptions.UserProfileNotFoundException;
import com.greenkawsay.users.domain.models.UserProfile;
import com.greenkawsay.users.domain.valueobjects.UserId;
import com.greenkawsay.users.infrastructure.adapters.in.web.dto.request.CreateUserProfileRequest;
import com.greenkawsay.users.infrastructure.adapters.in.web.dto.request.UpdateUserProfileRequest;
import com.greenkawsay.users.infrastructure.adapters.in.web.dto.response.UserProfileResponse;
import com.greenkawsay.users.infrastructure.adapters.in.web.mappers.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST Controller for User Profile operations
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Profiles", description = "User Profile management endpoints")
public class UserProfileController {

    private final UserProfileServicePort userProfileService;
    private final UserMapper userMapper;

    @PostMapping
    @Operation(summary = "Create a new user profile")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User profile created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "409", description = "User profile already exists")
    })
    public ResponseEntity<UserProfileResponse> createUserProfile(
            @Valid @RequestBody CreateUserProfileRequest request) {
        var userProfile = userMapper.toDomain(request);
        var createdUserProfile = userProfileService.createUserProfile(userProfile);
        var response = userMapper.toResponse(createdUserProfile);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get user profile by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User profile found"),
        @ApiResponse(responseCode = "404", description = "User profile not found")
    })
    public ResponseEntity<UserProfileResponse> getUserProfile(
            @Parameter(description = "User ID") @PathVariable UUID userId) {
        var userProfile = userProfileService.getUserProfileById(new UserId(userId))
                .orElseThrow(() -> new UserProfileNotFoundException("User profile not found with ID: " + userId));
        var response = userMapper.toResponse(userProfile);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all user profiles with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User profiles retrieved successfully")
    })
    public ResponseEntity<Page<UserProfileResponse>> getAllUserProfiles(Pageable pageable) {
        var userProfiles = userProfileService.getAllUserProfiles(pageable);
        var responses = userProfiles.map(userMapper::toResponse);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{userId}")
    @Operation(summary = "Update user profile")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User profile updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "User profile not found")
    })
    public ResponseEntity<UserProfileResponse> updateUserProfile(
            @Parameter(description = "User ID") @PathVariable UUID userId,
            @Valid @RequestBody UpdateUserProfileRequest request) {
        var userProfile = userMapper.toDomain(userId.toString(), request);
        var updatedUserProfile = userProfileService.updateUserProfile(new UserId(userId), userProfile);
        var response = userMapper.toResponse(updatedUserProfile);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Delete user profile")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User profile deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User profile not found")
    })
    public ResponseEntity<Void> deleteUserProfile(
            @Parameter(description = "User ID") @PathVariable UUID userId) {
        // Check if user exists first
        userProfileService.getUserProfileById(new UserId(userId))
                .orElseThrow(() -> new UserProfileNotFoundException("User profile not found with ID: " + userId));
        userProfileService.deactivateUserProfile(new UserId(userId));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{userId}/activate")
    @Operation(summary = "Activate user profile")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User profile activated successfully"),
        @ApiResponse(responseCode = "404", description = "User profile not found")
    })
    public ResponseEntity<UserProfileResponse> activateUserProfile(
            @Parameter(description = "User ID") @PathVariable UUID userId) {
        // Check if user exists first
        var existingUser = userProfileService.getUserProfileById(new UserId(userId))
                .orElseThrow(() -> new UserProfileNotFoundException("User profile not found with ID: " + userId));
        userProfileService.activateUserProfile(new UserId(userId));
        var response = userMapper.toResponse(existingUser);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{userId}/deactivate")
    @Operation(summary = "Deactivate user profile")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User profile deactivated successfully"),
        @ApiResponse(responseCode = "404", description = "User profile not found")
    })
    public ResponseEntity<UserProfileResponse> deactivateUserProfile(
            @Parameter(description = "User ID") @PathVariable UUID userId) {
        // Check if user exists first
        var existingUser = userProfileService.getUserProfileById(new UserId(userId))
                .orElseThrow(() -> new UserProfileNotFoundException("User profile not found with ID: " + userId));
        userProfileService.deactivateUserProfile(new UserId(userId));
        var response = userMapper.toResponse(existingUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/keycloak/{keycloakId}")
    @Operation(summary = "Get user profile by Keycloak ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User profile found"),
        @ApiResponse(responseCode = "404", description = "User profile not found")
    })
    public ResponseEntity<UserProfileResponse> getUserProfileByKeycloakId(
            @Parameter(description = "Keycloak ID") @PathVariable String keycloakId) {
        var userProfile = userProfileService.getUserProfileByKeycloakId(keycloakId)
                .orElseThrow(() -> new UserProfileNotFoundException("User profile not found with Keycloak ID: " + keycloakId));
        var response = userMapper.toResponse(userProfile);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Get user profile by email")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User profile found"),
        @ApiResponse(responseCode = "404", description = "User profile not found")
    })
    public ResponseEntity<UserProfileResponse> getUserProfileByEmail(
            @Parameter(description = "Email address") @PathVariable String email) {
        var userProfile = userProfileService.getUserProfileByEmail(email)
                .orElseThrow(() -> new UserProfileNotFoundException("User profile not found with email: " + email));
        var response = userMapper.toResponse(userProfile);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/role/{role}")
    @Operation(summary = "Get user profiles by role with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User profiles retrieved successfully")
    })
    public ResponseEntity<Page<UserProfileResponse>> getUserProfilesByRole(
            @Parameter(description = "User role") @PathVariable String role,
            Pageable pageable) {
        var userProfiles = userProfileService.getUserProfilesByRole(role, pageable);
        var responses = userProfiles.map(userMapper::toResponse);
        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/{userId}/impact-score")
    @Operation(summary = "Update user impact score")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Impact score updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid impact score"),
        @ApiResponse(responseCode = "404", description = "User profile not found")
    })
    public ResponseEntity<UserProfileResponse> updateImpactScore(
            @Parameter(description = "User ID") @PathVariable UUID userId,
            @RequestParam int impactScore) {
        var updatedUserProfile = userProfileService.updateImpactScore(new UserId(userId), impactScore);
        var response = userMapper.toResponse(updatedUserProfile);
        return ResponseEntity.ok(response);
    }
}