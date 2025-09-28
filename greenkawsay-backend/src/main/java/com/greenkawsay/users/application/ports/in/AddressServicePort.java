package com.greenkawsay.users.application.ports.in;

import com.greenkawsay.users.domain.models.UserAddress;
import com.greenkawsay.users.domain.valueobjects.AddressId;
import com.greenkawsay.users.domain.valueobjects.UserId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service port interface for Address operations
 */
public interface AddressServicePort {
    
    /**
     * Create a new address for a user
     */
    UserAddress createAddress(UserAddress address);
    
    /**
     * Update an existing address
     */
    UserAddress updateAddress(AddressId addressId, UserAddress address);
    
    /**
     * Get address by ID
     */
    Optional<UserAddress> getAddressById(AddressId addressId);
    
    /**
     * Get all addresses for a user
     */
    List<UserAddress> getAddressesByUserId(UserId userId);
    
    /**
     * Get addresses for a user with pagination
     */
    Page<UserAddress> getAddressesByUserId(UserId userId, Pageable pageable);
    
    /**
     * Get default address for a user
     */
    Optional<UserAddress> getDefaultAddressByUserId(UserId userId);
    
    /**
     * Set an address as default for a user
     */
    UserAddress setDefaultAddress(AddressId addressId);
    
    /**
     * Delete an address
     */
    void deleteAddress(AddressId addressId);
    
    /**
     * Check if address exists
     */
    boolean existsById(AddressId addressId);
    
    /**
     * Check if user has any addresses
     */
    boolean hasAddresses(UserId userId);
    
    /**
     * Get addresses by city
     */
    Page<UserAddress> getAddressesByCity(String city, Pageable pageable);
    
    /**
     * Get addresses by country
     */
    Page<UserAddress> getAddressesByCountry(String country, Pageable pageable);
}