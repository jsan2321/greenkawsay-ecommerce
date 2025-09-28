package com.greenkawsay.users.domain.models;

import com.greenkawsay.users.domain.models.WishlistItem;
import com.greenkawsay.users.domain.valueobjects.UserId;
import com.greenkawsay.users.domain.valueobjects.WishlistId;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Domain model representing a user wishlist
 * Immutable and thread-safe
 */
public class Wishlist {
    private final WishlistId id;
    private final UserId userId;
    private final String name;
    private final String description;
    private final boolean isPublic;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final List<WishlistItem> items;

    public Wishlist(WishlistId id, UserId userId, String name, String description, 
                   boolean isPublic, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = Objects.requireNonNull(id, "Wishlist ID cannot be null");
        this.userId = Objects.requireNonNull(userId, "User ID cannot be null");
        this.name = Objects.requireNonNull(name, "Name cannot be null").trim();
        this.description = description != null ? description.trim() : null;
        this.isPublic = isPublic;
        this.createdAt = Objects.requireNonNull(createdAt, "Created at cannot be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "Updated at cannot be null");
        this.items = new ArrayList<>();
        
        validate();
    }

    private void validate() {
        if (name.isBlank()) {
            throw new IllegalArgumentException("Wishlist name cannot be empty");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("Wishlist name cannot exceed 100 characters");
        }
        if (description != null && description.length() > 500) {
            throw new IllegalArgumentException("Wishlist description cannot exceed 500 characters");
        }
    }

    // Getters
    public WishlistId getId() { return id; }
    public UserId getUserId() { return userId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public boolean isPublic() { return isPublic; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public List<WishlistItem> getItems() { return Collections.unmodifiableList(items); }

    // Business methods
    public Wishlist updateName(String newName) {
        return new Wishlist(
            this.id, this.userId, newName, this.description, this.isPublic, 
            this.createdAt, LocalDateTime.now()
        );
    }

    public Wishlist updateDescription(String newDescription) {
        return new Wishlist(
            this.id, this.userId, this.name, newDescription, this.isPublic, 
            this.createdAt, LocalDateTime.now()
        );
    }

    public Wishlist makePublic() {
        return new Wishlist(
            this.id, this.userId, this.name, this.description, true, 
            this.createdAt, LocalDateTime.now()
        );
    }

    public Wishlist makePrivate() {
        return new Wishlist(
            this.id, this.userId, this.name, this.description, false, 
            this.createdAt, LocalDateTime.now()
        );
    }

    public boolean hasDescription() {
        return description != null && !description.isBlank();
    }

    public int getItemCount() {
        return items.size();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wishlist wishlist = (Wishlist) o;
        return Objects.equals(id, wishlist.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Wishlist{" +
                "id=" + id +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", isPublic=" + isPublic +
                '}';
    }
}