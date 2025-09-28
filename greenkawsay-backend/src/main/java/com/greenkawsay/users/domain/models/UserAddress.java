package com.greenkawsay.users.domain.models;

import com.greenkawsay.shared.domain.valueobjects.Address;
import com.greenkawsay.users.domain.valueobjects.AddressId;
import com.greenkawsay.users.domain.valueobjects.UserId;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Domain model representing a user address
 * Immutable and thread-safe
 */
public class UserAddress {
    private final AddressId id;
    private final UserId userId;
    private final Address address;
    private final boolean isDefault;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public UserAddress(AddressId id, UserId userId, Address address, boolean isDefault,
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = Objects.requireNonNull(id, "Address ID cannot be null");
        this.userId = Objects.requireNonNull(userId, "User ID cannot be null");
        this.address = Objects.requireNonNull(address, "Address cannot be null");
        this.isDefault = isDefault;
        this.createdAt = Objects.requireNonNull(createdAt, "Created at cannot be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "Updated at cannot be null");
    }

    // Getters
    public AddressId getId() { return id; }
    public UserId getUserId() { return userId; }
    public Address getAddress() { return address; }
    public boolean isDefault() { return isDefault; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Business methods
    public UserAddress markAsDefault() {
        return new UserAddress(
            this.id, this.userId, this.address, true, this.createdAt, LocalDateTime.now()
        );
    }

    public UserAddress markAsNonDefault() {
        return new UserAddress(
            this.id, this.userId, this.address, false, this.createdAt, LocalDateTime.now()
        );
    }

    public UserAddress updateAddress(Address newAddress) {
        return new UserAddress(
            this.id, this.userId, newAddress, this.isDefault, this.createdAt, LocalDateTime.now()
        );
    }

    public boolean isInCountry(String countryCode) {
        return address.isInCountry(countryCode);
    }

    public String getFormattedAddress() {
        return address.toFormattedString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAddress userAddress = (UserAddress) o;
        return Objects.equals(id, userAddress.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "UserAddress{" +
                "id=" + id +
                ", userId=" + userId +
                ", address=" + address +
                ", isDefault=" + isDefault +
                '}';
    }
}