package com.balajimart.service;

import com.balajimart.dao.CartDAO;
import com.balajimart.dao.ProductDAO;
import com.balajimart.model.CartItem;
import com.balajimart.model.Product;

import java.math.BigDecimal;
import java.util.List;

public class CartService {

    private final CartDAO cartDAO;
    private final ProductDAO productDAO;

    public CartService() {
        this.cartDAO = new CartDAO();
        this.productDAO = new ProductDAO();
    }

    public CartService(CartDAO cartDAO, ProductDAO productDAO) {
        this.cartDAO = cartDAO;
        this.productDAO = productDAO;
    }

    public List<CartItem> getUserCart(Long userId) {
        return cartDAO.findByUserId(userId);
    }

    public void addToCart(Long userId, Long productId, int quantity) throws IllegalArgumentException {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        Product product = productDAO.findById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product not found");
        }

        if (product.getStock() <= 0) {
            throw new IllegalArgumentException("Product is out of stock");
        }

        CartItem existingItem = cartDAO.findItem(userId, productId);
        int targetQuantity = quantity;
        if (existingItem != null) {
            targetQuantity = existingItem.getQuantity() + quantity;
        }

        if (targetQuantity > product.getStock()) {
            throw new IllegalArgumentException("Requested quantity (" + targetQuantity + ") exceeds available stock (" + product.getStock() + ")");
        }

        if (existingItem != null) {
            cartDAO.updateQuantity(existingItem.getId(), userId, targetQuantity);
        } else {
            cartDAO.addItem(userId, productId, quantity);
        }
    }

    public void updateCartQuantity(Long cartItemId, Long userId, int newQuantity) throws IllegalArgumentException {
        if (newQuantity <= 0) {
            removeFromCart(cartItemId, userId);
            return;
        }

        List<CartItem> items = cartDAO.findByUserId(userId);
        CartItem targetItem = items.stream()
                .filter(i -> i.getId().equals(cartItemId))
                .findFirst()
                .orElse(null);

        if (targetItem == null) {
            throw new IllegalArgumentException("Cart item not found");
        }

        Product product = productDAO.findById(targetItem.getProductId());
        if (product != null && newQuantity > product.getStock()) {
            throw new IllegalArgumentException("Quantity cannot exceed available stock (" + product.getStock() + ")");
        }

        cartDAO.updateQuantity(cartItemId, userId, newQuantity);
    }

    public void removeFromCart(Long cartItemId, Long userId) {
        cartDAO.removeItem(cartItemId, userId);
    }

    public BigDecimal calculateTotal(List<CartItem> cartItems) {
        BigDecimal total = BigDecimal.ZERO;
        if (cartItems != null) {
            for (CartItem item : cartItems) {
                total = total.add(item.getSubtotal());
            }
        }
        return total;
    }
}
