package com.greenkawsay.users.application.services;

import com.greenkawsay.shared.domain.valueobjects.Address;
import com.greenkawsay.users.application.ports.in.AddressServicePort;
import com.greenkawsay.users.domain.exceptions.AddressNotFoundException;
import com.greenkawsay.users.domain.exceptions.UserProfileNotFoundException;
import com.greenkawsay.users.domain.models.UserAddress;
import com.greenkawsay.users.domain.repositories.AddressRepository;
import com.greenkawsay.users.domain.repositories.UserProfileRepository;
import com.greenkawsay.users.domain.valueobjects.AddressId;
import com.greenkawsay.users.domain.valueobjects.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Application service for Address operations
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AddressApplicationService implements AddressServicePort {

    private final AddressRepository addressRepository;
    private final UserProfileRepository userProfileRepository;

    @Override
    public UserAddress createAddress(UserAddress address) {
        log.info("Creating address for user ID: {}", address.getUserId().getValue());
        
        // Validate that user exists
        if (!userProfileRepository.findById(address.getUserId()).isPresent()) {
            throw new UserProfileNotFoundException("User profile with ID " + address.getUserId().getValue() + " not found");
        }
        
        // If this is set as default, unset other default addresses for this user
        if (address.isDefault()) {
            List<UserAddress> userAddresses = addressRepository.findByUserId(address.getUserId());
            userAddresses.stream()
                    .filter(UserAddress::isDefault)
                    .forEach(defaultAddress -> {
                        UserAddress updatedAddress = defaultAddress.markAsNonDefault();
                        addressRepository.save(updatedAddress);
                    });
        }
        
        UserAddress savedAddress = addressRepository.save(address);
        log.info("Address created successfully with ID: {}", savedAddress.getId().getValue());
        
        return savedAddress;
    }

    @Override
    public UserAddress updateAddress(AddressId addressId, UserAddress updatedAddress) {
        log.info("Updating address with ID: {}", addressId.getValue());
        
        UserAddress existingAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new AddressNotFoundException(addressId));
        
        // Update the address using domain methods
        UserAddress addressToUpdate = existingAddress.updateAddress(updatedAddress.getAddress());
        
        // Handle default address logic
        if (updatedAddress.isDefault()) {
            addressToUpdate = addressToUpdate.markAsDefault();
            
            // Unset other default addresses for this user
            List<UserAddress> userAddresses = addressRepository.findByUserId(existingAddress.getUserId());
            userAddresses.stream()
                    .filter(UserAddress::isDefault)
                    .filter(addr -> !addr.getId().equals(addressId))
                    .forEach(defaultAddress -> {
                        UserAddress updatedDefaultAddress = defaultAddress.markAsNonDefault();
                        addressRepository.save(updatedDefaultAddress);
                    });
        } else {
            addressToUpdate = addressToUpdate.markAsNonDefault();
        }
        
        UserAddress savedAddress = addressRepository.save(addressToUpdate);
        log.info("Address updated successfully with ID: {}", savedAddress.getId().getValue());
        
        return savedAddress;
    }

    @Override
    public Optional<UserAddress> getAddressById(AddressId addressId) {
        log.debug("Getting address by ID: {}", addressId.getValue());
        return addressRepository.findById(addressId);
    }

    @Override
    public List<UserAddress> getAddressesByUserId(UserId userId) {
        log.debug("Getting addresses for user ID: {}", userId.getValue());
        return addressRepository.findByUserId(userId);
    }

    @Override
    public Page<UserAddress> getAddressesByUserId(UserId userId, Pageable pageable) {
        log.debug("Getting addresses for user ID: {} with pagination", userId.getValue());
        List<UserAddress> userAddresses = addressRepository.findByUserId(userId);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), userAddresses.size());
        List<UserAddress> pageContent = userAddresses.subList(start, end);
        return new PageImpl<>(pageContent, pageable, userAddresses.size());
    }

    @Override
    public Optional<UserAddress> getDefaultAddressByUserId(UserId userId) {
        log.debug("Getting default address for user ID: {}", userId.getValue());
        return addressRepository.findDefaultByUserId(userId);
    }

    @Override
    public UserAddress setDefaultAddress(AddressId addressId) {
        log.info("Setting address as default with ID: {}", addressId.getValue());
        
        UserAddress address = addressRepository.findById(addressId)
                .orElseThrow(() -> new AddressNotFoundException(addressId));
        
        // Unset other default addresses for this user
        List<UserAddress> userAddresses = addressRepository.findByUserId(address.getUserId());
        userAddresses.stream()
                .filter(UserAddress::isDefault)
                .forEach(defaultAddress -> {
                    UserAddress updatedAddress = defaultAddress.markAsNonDefault();
                    addressRepository.save(updatedAddress);
                });
        
        // Set the specified address as default
        UserAddress defaultAddress = address.markAsDefault();
        UserAddress savedAddress = addressRepository.save(defaultAddress);
        log.info("Address set as default successfully with ID: {}", addressId.getValue());
        
        return savedAddress;
    }

    @Override
    public void deleteAddress(AddressId addressId) {
        log.info("Deleting address with ID: {}", addressId.getValue());
        
        UserAddress address = addressRepository.findById(addressId)
                .orElseThrow(() -> new AddressNotFoundException(addressId));
        
        addressRepository.deleteById(addressId);
        log.info("Address deleted successfully with ID: {}", addressId.getValue());
    }

    @Override
    public boolean existsById(AddressId addressId) {
        return addressRepository.findById(addressId).isPresent();
    }

    @Override
    public boolean hasAddresses(UserId userId) {
        return !addressRepository.findByUserId(userId).isEmpty();
    }

    @Override
    public Page<UserAddress> getAddressesByCity(String city, Pageable pageable) {
        log.debug("Getting addresses by city: {}", city);
        // Since we don't have a findAll() method, we need to get addresses by iterating through users
        // This is a limitation - in a real implementation, we would add a findByCity method to the repository
        List<UserAddress> addressesInCity = userProfileRepository.findAll()
                .stream()
                .flatMap(user -> addressRepository.findByUserId(user.getId()).stream())
                .filter(address -> address.getAddress().getCity().equalsIgnoreCase(city))
                .toList();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), addressesInCity.size());
        List<UserAddress> pageContent = addressesInCity.subList(start, end);
        return new PageImpl<>(pageContent, pageable, addressesInCity.size());
    }

    @Override
    public Page<UserAddress> getAddressesByCountry(String country, Pageable pageable) {
        log.debug("Getting addresses by country: {}", country);
        List<UserAddress> addressesInCountry = addressRepository.findByCountry(country);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), addressesInCountry.size());
        List<UserAddress> pageContent = addressesInCountry.subList(start, end);
        return new PageImpl<>(pageContent, pageable, addressesInCountry.size());
    }
}