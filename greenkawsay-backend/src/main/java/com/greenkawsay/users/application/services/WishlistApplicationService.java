package com.greenkawsay.users.application.services;

import com.greenkawsay.catalog.domain.valueobjects.ProductId;
import com.greenkawsay.users.application.ports.in.WishlistServicePort;
import com.greenkawsay.users.domain.exceptions.UserProfileNotFoundException;
import com.greenkawsay.users.domain.exceptions.WishlistNotFoundException;
import com.greenkawsay.users.domain.models.Wishlist;
import com.greenkawsay.users.domain.models.WishlistItem;
import com.greenkawsay.users.domain.repositories.UserProfileRepository;
import com.greenkawsay.users.domain.repositories.WishlistItemRepository;
import com.greenkawsay.users.domain.repositories.WishlistRepository;
import com.greenkawsay.users.domain.valueobjects.UserId;
import com.greenkawsay.users.domain.valueobjects.WishlistId;
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
 * Application service for Wishlist operations
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class WishlistApplicationService implements WishlistServicePort {

    private final WishlistRepository wishlistRepository;
    private final WishlistItemRepository wishlistItemRepository;
    private final UserProfileRepository userProfileRepository;

    @Override
    public Wishlist createWishlist(Wishlist wishlist) {
        log.info("Creating wishlist for user ID: {}", wishlist.getUserId().getValue());
        
        // Validate that user exists
        if (!userProfileRepository.findById(wishlist.getUserId()).isPresent()) {
            throw new UserProfileNotFoundException("User profile with ID " + wishlist.getUserId().getValue() + " not found");
        }
        
        Wishlist savedWishlist = wishlistRepository.save(wishlist);
        log.info("Wishlist created successfully with ID: {}", savedWishlist.getId().getValue());
        
        return savedWishlist;
    }

    @Override
    public Wishlist updateWishlist(WishlistId wishlistId, Wishlist updatedWishlist) {
        log.info("Updating wishlist with ID: {}", wishlistId.getValue());
        
        Wishlist existingWishlist = wishlistRepository.findById(wishlistId)
                .orElseThrow(() -> new WishlistNotFoundException(wishlistId));
        
        // Use domain methods to update the wishlist
        Wishlist wishlistToUpdate = existingWishlist.updateName(updatedWishlist.getName());
        if (updatedWishlist.getDescription() != null) {
            wishlistToUpdate = wishlistToUpdate.updateDescription(updatedWishlist.getDescription());
        }
        if (updatedWishlist.isPublic()) {
            wishlistToUpdate = wishlistToUpdate.makePublic();
        } else {
            wishlistToUpdate = wishlistToUpdate.makePrivate();
        }
        
        Wishlist savedWishlist = wishlistRepository.save(wishlistToUpdate);
        log.info("Wishlist updated successfully with ID: {}", savedWishlist.getId().getValue());
        
        return savedWishlist;
    }

    @Override
    public Optional<Wishlist> getWishlistById(WishlistId wishlistId) {
        log.debug("Getting wishlist by ID: {}", wishlistId.getValue());
        return wishlistRepository.findById(wishlistId);
    }

    @Override
    public List<Wishlist> getWishlistsByUserId(UserId userId) {
        log.debug("Getting wishlists for user ID: {}", userId.getValue());
        return wishlistRepository.findByUserId(userId);
    }

    @Override
    public Page<Wishlist> getWishlistsByUserId(UserId userId, Pageable pageable) {
        log.debug("Getting wishlists for user ID: {} with pagination", userId.getValue());
        List<Wishlist> userWishlists = wishlistRepository.findByUserId(userId);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), userWishlists.size());
        List<Wishlist> pageContent = userWishlists.subList(start, end);
        return new PageImpl<>(pageContent, pageable, userWishlists.size());
    }

    @Override
    public Optional<Wishlist> getDefaultWishlistByUserId(UserId userId) {
        log.debug("Getting default wishlist for user ID: {}", userId.getValue());
        // In this implementation, we'll consider the first wishlist as default
        List<Wishlist> userWishlists = wishlistRepository.findByUserId(userId);
        return userWishlists.stream().findFirst();
    }

    @Override
    public Wishlist setDefaultWishlist(WishlistId wishlistId) {
        log.info("Setting wishlist as default with ID: {}", wishlistId.getValue());
        
        Wishlist wishlist = wishlistRepository.findById(wishlistId)
                .orElseThrow(() -> new WishlistNotFoundException(wishlistId));
        
        // In this simple implementation, we just return the wishlist
        // In a more complex implementation, we might mark it as default in the database
        log.info("Wishlist set as default successfully with ID: {}", wishlistId.getValue());
        
        return wishlist;
    }

    @Override
    public void deleteWishlist(WishlistId wishlistId) {
        log.info("Deleting wishlist with ID: {}", wishlistId.getValue());
        
        Wishlist wishlist = wishlistRepository.findById(wishlistId)
                .orElseThrow(() -> new WishlistNotFoundException(wishlistId));
        
        // Delete all wishlist items first
        List<WishlistItem> wishlistItems = wishlistItemRepository.findByWishlistId(wishlistId);
        wishlistItems.forEach(item -> wishlistItemRepository.deleteByWishlistIdAndProductId(wishlistId, item.getProductId()));
        
        wishlistRepository.deleteById(wishlistId);
        log.info("Wishlist deleted successfully with ID: {}", wishlistId.getValue());
    }

    @Override
    public WishlistItem addItemToWishlist(WishlistId wishlistId, ProductId productId) {
        log.info("Adding item to wishlist with ID: {}", wishlistId.getValue());
        
        Wishlist wishlist = wishlistRepository.findById(wishlistId)
                .orElseThrow(() -> new WishlistNotFoundException(wishlistId));
        
        WishlistItem wishlistItem = new WishlistItem(
            wishlistId, productId, LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now()
        );
        
        WishlistItem savedItem = wishlistItemRepository.save(wishlistItem);
        log.info("Item added to wishlist successfully");
        
        return savedItem;
    }

    @Override
    public void removeItemFromWishlist(WishlistId wishlistId, ProductId productId) {
        log.info("Removing item from wishlist with ID: {}", wishlistId.getValue());
        
        Wishlist wishlist = wishlistRepository.findById(wishlistId)
                .orElseThrow(() -> new WishlistNotFoundException(wishlistId));
        
        wishlistItemRepository.deleteByWishlistIdAndProductId(wishlistId, productId);
        log.info("Item removed from wishlist successfully");
    }

    @Override
    public List<WishlistItem> getWishlistItems(WishlistId wishlistId) {
        log.debug("Getting items for wishlist with ID: {}", wishlistId.getValue());
        return wishlistItemRepository.findByWishlistId(wishlistId);
    }

    @Override
    public Page<WishlistItem> getWishlistItems(WishlistId wishlistId, Pageable pageable) {
        log.debug("Getting items for wishlist with ID: {} with pagination", wishlistId.getValue());
        List<WishlistItem> wishlistItems = wishlistItemRepository.findByWishlistId(wishlistId);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), wishlistItems.size());
        List<WishlistItem> pageContent = wishlistItems.subList(start, end);
        return new PageImpl<>(pageContent, pageable, wishlistItems.size());
    }

    @Override
    public boolean isProductInWishlist(WishlistId wishlistId, ProductId productId) {
        log.debug("Checking if product {} is in wishlist {}", productId.getValue(), wishlistId.getValue());
        return wishlistItemRepository.findByWishlistIdAndProductId(wishlistId, productId).isPresent();
    }

    @Override
    public Optional<WishlistItem> getWishlistItemByProductId(WishlistId wishlistId, ProductId productId) {
        log.debug("Getting wishlist item for product {} in wishlist {}", productId.getValue(), wishlistId.getValue());
        return wishlistItemRepository.findByWishlistIdAndProductId(wishlistId, productId);
    }

    @Override
    public Page<Wishlist> getPublicWishlists(Pageable pageable) {
        log.debug("Getting public wishlists with pagination");
        // Since we don't have a findAll() method, we need to get wishlists by iterating through users
        List<Wishlist> allWishlists = userProfileRepository.findAll()
                .stream()
                .flatMap(user -> wishlistRepository.findByUserId(user.getId()).stream())
                .toList();
        List<Wishlist> publicWishlists = allWishlists.stream()
                .filter(Wishlist::isPublic)
                .toList();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), publicWishlists.size());
        List<Wishlist> pageContent = publicWishlists.subList(start, end);
        return new PageImpl<>(pageContent, pageable, publicWishlists.size());
    }

    @Override
    public Page<Wishlist> getWishlistsByNameContaining(String name, Pageable pageable) {
        log.debug("Searching wishlists by name containing: {}", name);
        // Since we don't have a search method, we need to get wishlists by iterating through users
        List<Wishlist> allWishlists = userProfileRepository.findAll()
                .stream()
                .flatMap(user -> wishlistRepository.findByUserId(user.getId()).stream())
                .toList();
        List<Wishlist> matchingWishlists = allWishlists.stream()
                .filter(wishlist -> wishlist.getName().toLowerCase().contains(name.toLowerCase()))
                .toList();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), matchingWishlists.size());
        List<Wishlist> pageContent = matchingWishlists.subList(start, end);
        return new PageImpl<>(pageContent, pageable, matchingWishlists.size());
    }

    @Override
    public boolean existsById(WishlistId wishlistId) {
        return wishlistRepository.findById(wishlistId).isPresent();
    }

    @Override
    public boolean hasWishlists(UserId userId) {
        return !wishlistRepository.findByUserId(userId).isEmpty();
    }
}