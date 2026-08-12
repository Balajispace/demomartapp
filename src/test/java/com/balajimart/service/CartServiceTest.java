package com.balajimart.service;

import com.balajimart.dao.CartDAO;
import com.balajimart.dao.ProductDAO;
import com.balajimart.model.CartItem;
import com.balajimart.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CartServiceTest {

    private CartService cartService;
    private StubCartDAO cartDAO;
    private StubProductDAO productDAO;

    private static class StubCartDAO extends CartDAO {
        private final Map<Long, List<CartItem>> userCarts = new HashMap<>();
        private long idCounter = 1;

        @Override
        public List<CartItem> findByUserId(Long userId) {
            return userCarts.getOrDefault(userId, new ArrayList<>());
        }

        @Override
        public CartItem findItem(Long userId, Long productId) {
            return findByUserId(userId).stream()
                    .filter(i -> i.getProductId().equals(productId))
                    .findFirst().orElse(null);
        }

        @Override
        public boolean addItem(Long userId, Long productId, int quantity) {
            List<CartItem> cart = userCarts.computeIfAbsent(userId, k -> new ArrayList<>());
            CartItem item = new CartItem();
            item.setId(idCounter++);
            item.setUserId(userId);
            item.setProductId(productId);
            item.setQuantity(quantity);

            Product p = new Product();
            p.setId(productId);
            p.setPrice(new BigDecimal("500.00"));
            item.setProduct(p);

            cart.add(item);
            return true;
        }

        @Override
        public boolean updateQuantity(Long cartItemId, Long userId, int newQuantity) {
            List<CartItem> cart = findByUserId(userId);
            for (CartItem item : cart) {
                if (item.getId().equals(cartItemId)) {
                    item.setQuantity(newQuantity);
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean removeItem(Long cartItemId, Long userId) {
            List<CartItem> cart = findByUserId(userId);
            return cart.removeIf(item -> item.getId().equals(cartItemId));
        }
    }

    private static class StubProductDAO extends ProductDAO {
        private final Map<Long, Product> products = new HashMap<>();

        public void addProduct(Product p) {
            products.put(p.getId(), p);
        }

        @Override
        public Product findById(Long id) {
            return products.get(id);
        }
    }

    @BeforeEach
    void setUp() {
        cartDAO = new StubCartDAO();
        productDAO = new StubProductDAO();
        cartService = new CartService(cartDAO, productDAO);

        Product p1 = new Product();
        p1.setId(101L);
        p1.setName("Wireless Mouse");
        p1.setPrice(new BigDecimal("799.00"));
        p1.setStock(10);
        productDAO.addProduct(p1);

        Product p2 = new Product();
        p2.setId(102L);
        p2.setName("Out of Stock Laptop");
        p2.setPrice(new BigDecimal("45000.00"));
        p2.setStock(0);
        productDAO.addProduct(p2);
    }

    @Test
    @DisplayName("Should successfully add product to user cart")
    void testAddToCartSuccess() {
        cartService.addToCart(1L, 101L, 2);
        List<CartItem> cart = cartService.getUserCart(1L);

        assertEquals(1, cart.size());
        assertEquals(2, cart.get(0).getQuantity());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when adding out of stock product")
    void testAddToCartOutOfStock() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                cartService.addToCart(1L, 102L, 1)
        );
        assertEquals("Product is out of stock", ex.getMessage());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when requested quantity exceeds available stock")
    void testAddToCartExceedingStock() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                cartService.addToCart(1L, 101L, 15)
        );
        assertTrue(ex.getMessage().contains("exceeds available stock"));
    }

    @Test
    @DisplayName("Should calculate total correctly for multiple cart items")
    void testCalculateTotal() {
        CartItem item1 = new CartItem();
        Product p1 = new Product();
        p1.setPrice(new BigDecimal("100.00"));
        item1.setProduct(p1);
        item1.setQuantity(3); // 300.00

        CartItem item2 = new CartItem();
        Product p2 = new Product();
        p2.setPrice(new BigDecimal("250.50"));
        item2.setProduct(p2);
        item2.setQuantity(2); // 501.00

        BigDecimal total = cartService.calculateTotal(Arrays.asList(item1, item2));
        assertEquals(new BigDecimal("801.00"), total);
    }
}
