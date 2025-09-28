package com.greenkawsay.users.domain.repositories;

import com.greenkawsay.users.domain.models.UserAddress;
import com.greenkawsay.users.domain.valueobjects.AddressId;
import com.greenkawsay.users.domain.valueobjects.UserId;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for UserAddress domain entities
 * Defines the contract for persistence operations
 */
public interface AddressRepository {
    
    /**
     * Save an address
     */
    UserAddress save(UserAddress address);
    
    /**
     * Find address by ID
     */
    Optional<UserAddress> findById(AddressId addressId);
    
    /**
     * Find addresses by user ID
     */
    List<UserAddress> findByUserId(UserId userId);
    
    /**
     * Find default address for user
     */
    Optional<UserAddress> findDefaultByUserId(UserId userId);
    
    /**
     * Find addresses by country
     */
    List<UserAddress> findByCountry(String countryCode);
    
    /**
     * Check if address exists by ID
     */
    boolean existsById(AddressId addressId);
    
    /**
     * Delete address by ID
     */
    void deleteById(AddressId addressId);
    
    /**
     * Delete addresses by user ID
     */
    void deleteByUserId(UserId userId);
    
    /**
     * Count addresses for user
     */
    long countByUserId(UserId userId);
    
    /**
     * Count addresses by country
     */
    long countByCountry(String countryCode);
}