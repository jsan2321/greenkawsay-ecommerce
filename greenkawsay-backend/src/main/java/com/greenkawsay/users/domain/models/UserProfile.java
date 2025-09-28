package com.greenkawsay.users.domain.models;

import com.greenkawsay.users.domain.models.UserAddress;
import com.greenkawsay.users.domain.models.Wishlist;
import com.greenkawsay.shared.domain.valueobjects.Email;
import com.greenkawsay.users.domain.valueobjects.ImpactScore;
import com.greenkawsay.users.domain.valueobjects.Role;
import com.greenkawsay.users.domain.valueobjects.UserId;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Domain model representing a user profile
 * Immutable and thread-safe
 */
public class UserProfile {
    private final UserId id;
    private final String keycloakId;
    private final Email email;
    private final String firstName;
    private final String lastName;
    private final String avatarUrl;
    private final Role role;
    private final ImpactScore impactScoreTotal;
    private final boolean isActive;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final List<UserAddress> addresses;
    private final List<Wishlist> wishlists;

    public UserProfile(UserId id, String keycloakId, Email email, String firstName, String lastName, 
                      String avatarUrl, Role role, ImpactScore impactScoreTotal, boolean isActive,
                      LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = Objects.requireNonNull(id, "User ID cannot be null");
        this.keycloakId = Objects.requireNonNull(keycloakId, "Keycloak ID cannot be null").trim();
        this.email = Objects.requireNonNull(email, "Email cannot be null");
        this.firstName = Objects.requireNonNull(firstName, "First name cannot be null").trim();
        this.lastName = Objects.requireNonNull(lastName, "Last name cannot be null").trim();
        this.avatarUrl = avatarUrl != null ? avatarUrl.trim() : null;
        this.role = Objects.requireNonNull(role, "Role cannot be null");
        this.impactScoreTotal = Objects.requireNonNull(impactScoreTotal, "Impact score cannot be null");
        this.isActive = isActive;
        this.createdAt = Objects.requireNonNull(createdAt, "Created at cannot be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "Updated at cannot be null");
        this.addresses = new ArrayList<>();
        this.wishlists = new ArrayList<>();
        
        validate();
    }

    private void validate() {
        if (keycloakId.isBlank()) {
            throw new IllegalArgumentException("Keycloak ID cannot be empty");
        }
        if (firstName.isBlank()) {
            throw new IllegalArgumentException("First name cannot be empty");
        }
        if (lastName.isBlank()) {
            throw new IllegalArgumentException("Last name cannot be empty");
        }
        if (keycloakId.length() > 255) {
            throw new IllegalArgumentException("Keycloak ID cannot exceed 255 characters");
        }
        if (firstName.length() > 100) {
            throw new IllegalArgumentException("First name cannot exceed 100 characters");
        }
        if (lastName.length() > 100) {
            throw new IllegalArgumentException("Last name cannot exceed 100 characters");
        }
        if (avatarUrl != null && avatarUrl.length() > 500) {
            throw new IllegalArgumentException("Avatar URL cannot exceed 500 characters");
        }
    }

    // Getters
    public UserId getId() { return id; }
    public String getKeycloakId() { return keycloakId; }
    public Email getEmail() { return email; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getAvatarUrl() { return avatarUrl; }
    public Role getRole() { return role; }
    public ImpactScore getImpactScoreTotal() { return impactScoreTotal; }
    public boolean isActive() { return isActive; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public List<UserAddress> getAddresses() { return Collections.unmodifiableList(addresses); }
    public List<Wishlist> getWishlists() { return Collections.unmodifiableList(wishlists); }

    // Business methods
    public UserProfile updateProfile(String firstName, String lastName, String avatarUrl) {
        return new UserProfile(
            this.id, this.keycloakId, this.email, firstName, lastName, avatarUrl,
            this.role, this.impactScoreTotal, this.isActive, this.createdAt, LocalDateTime.now()
        );
    }

    public UserProfile updateRole(Role newRole) {
        return new UserProfile(
            this.id, this.keycloakId, this.email, this.firstName, this.lastName, this.avatarUrl,
            newRole, this.impactScoreTotal, this.isActive, this.createdAt, LocalDateTime.now()
        );
    }

    public UserProfile addImpactScore(ImpactScore scoreToAdd) {
        ImpactScore newScore = this.impactScoreTotal.add(scoreToAdd);
        return new UserProfile(
            this.id, this.keycloakId, this.email, this.firstName, this.lastName, this.avatarUrl,
            this.role, newScore, this.isActive, this.createdAt, LocalDateTime.now()
        );
    }

    public UserProfile deactivate() {
        return new UserProfile(
            this.id, this.keycloakId, this.email, this.firstName, this.lastName, this.avatarUrl,
            this.role, this.impactScoreTotal, false, this.createdAt, LocalDateTime.now()
        );
    }

    public UserProfile activate() {
        return new UserProfile(
            this.id, this.keycloakId, this.email, this.firstName, this.lastName, this.avatarUrl,
            this.role, this.impactScoreTotal, true, this.createdAt, LocalDateTime.now()
        );
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public boolean hasAvatar() {
        return avatarUrl != null && !avatarUrl.isBlank();
    }

    public boolean isVendor() {
        return role.isVendor();
    }

    public boolean isAdmin() {
        return role.isAdmin();
    }

    public boolean isCustomer() {
        return role.isCustomer();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserProfile that = (UserProfile) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "id=" + id +
                ", email=" + email +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", role=" + role +
                '}';
    }
}