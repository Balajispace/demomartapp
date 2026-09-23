package com.balajimart.service;

import com.balajimart.dao.WishlistDAO;
import com.balajimart.model.Wishlist;

import java.util.List;
import java.util.Set;

public class WishlistService {

    private final WishlistDAO wishlistDAO;

    public WishlistService() {
        this.wishlistDAO = new WishlistDAO();
    }

    public WishlistService(WishlistDAO wishlistDAO) {
        this.wishlistDAO = wishlistDAO;
    }

    public List<Wishlist> getUserWishlist(Long userId) {
        if (userId == null) {
            return List.of();
        }
        return wishlistDAO.findByUserId(userId);
    }

    public boolean addToWishlist(Long userId, Long productId) throws IllegalArgumentException {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User must be logged in to manage wishlist");
        }
        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("Invalid product ID");
        }
        return wishlistDAO.addWishlistItem(userId, productId);
    }

    public boolean removeFromWishlist(Long userId, Long productId) throws IllegalArgumentException {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User must be logged in to manage wishlist");
        }
        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("Invalid product ID");
        }
        return wishlistDAO.removeWishlistItem(userId, productId);
    }

    public boolean toggleWishlist(Long userId, Long productId) throws IllegalArgumentException {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User must be logged in to manage wishlist");
        }
        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("Invalid product ID");
        }
        if (wishlistDAO.isWishlisted(userId, productId)) {
            wishlistDAO.removeWishlistItem(userId, productId);
            return false; // Now removed
        } else {
            wishlistDAO.addWishlistItem(userId, productId);
            return true; // Now added
        }
    }

    public boolean isWishlisted(Long userId, Long productId) {
        if (userId == null || productId == null) return false;
        return wishlistDAO.isWishlisted(userId, productId);
    }

    public Set<Long> getWishlistedProductIds(Long userId) {
        return wishlistDAO.getWishlistedProductIds(userId);
    }
}
