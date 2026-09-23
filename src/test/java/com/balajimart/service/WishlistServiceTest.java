package com.balajimart.service;

import com.balajimart.dao.WishlistDAO;
import com.balajimart.model.Wishlist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class WishlistServiceTest {

    private WishlistService wishlistService;
    private StubWishlistDAO stubWishlistDAO;

    private static class StubWishlistDAO extends WishlistDAO {
        private final List<Wishlist> storage = new ArrayList<>();
        private long idCounter = 1;

        @Override
        public List<Wishlist> findByUserId(Long userId) {
            return storage.stream()
                    .filter(item -> item.getUserId().equals(userId))
                    .collect(Collectors.toList());
        }

        @Override
        public boolean addWishlistItem(Long userId, Long productId) {
            if (isWishlisted(userId, productId)) {
                return true;
            }
            Wishlist w = new Wishlist();
            w.setId(idCounter++);
            w.setUserId(userId);
            w.setProductId(productId);
            storage.add(w);
            return true;
        }

        @Override
        public boolean removeWishlistItem(Long userId, Long productId) {
            return storage.removeIf(item -> item.getUserId().equals(userId) && item.getProductId().equals(productId));
        }

        @Override
        public boolean isWishlisted(Long userId, Long productId) {
            return storage.stream().anyMatch(item -> item.getUserId().equals(userId) && item.getProductId().equals(productId));
        }

        @Override
        public Set<Long> getWishlistedProductIds(Long userId) {
            if (userId == null) return new HashSet<>();
            return storage.stream()
                    .filter(item -> item.getUserId().equals(userId))
                    .map(Wishlist::getProductId)
                    .collect(Collectors.toSet());
        }
    }

    @BeforeEach
    void setUp() {
        stubWishlistDAO = new StubWishlistDAO();
        wishlistService = new WishlistService(stubWishlistDAO);
    }

    @Test
    @DisplayName("Should add product to wishlist and prevent duplicates")
    void testAddWishlistAndDuplicatePrevention() {
        Long buyerId = 100L;
        Long productId = 5L;

        assertTrue(wishlistService.addToWishlist(buyerId, productId));
        assertTrue(wishlistService.isWishlisted(buyerId, productId));

        // Adding second time should be idempotent and not create duplicate
        assertTrue(wishlistService.addToWishlist(buyerId, productId));
        assertEquals(1, wishlistService.getUserWishlist(buyerId).size());
    }

    @Test
    @DisplayName("Should isolate wishlists between different buyers")
    void testWishlistBuyerIsolation() {
        Long buyerA = 101L;
        Long buyerB = 102L;
        Long productId = 10L;

        wishlistService.addToWishlist(buyerA, productId);

        assertTrue(wishlistService.isWishlisted(buyerA, productId));
        assertFalse(wishlistService.isWishlisted(buyerB, productId));
        assertEquals(1, wishlistService.getUserWishlist(buyerA).size());
        assertEquals(0, wishlistService.getUserWishlist(buyerB).size());
    }

    @Test
    @DisplayName("Should toggle wishlist state on/off")
    void testToggleWishlist() {
        Long buyerId = 200L;
        Long productId = 12L;

        boolean added = wishlistService.toggleWishlist(buyerId, productId);
        assertTrue(added);
        assertTrue(wishlistService.isWishlisted(buyerId, productId));

        boolean removed = wishlistService.toggleWishlist(buyerId, productId);
        assertFalse(removed);
        assertFalse(wishlistService.isWishlisted(buyerId, productId));
    }
}
