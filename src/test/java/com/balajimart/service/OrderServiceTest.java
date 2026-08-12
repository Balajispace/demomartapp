package com.balajimart.service;

import com.balajimart.dao.CartDAO;
import com.balajimart.dao.OrderDAO;
import com.balajimart.dao.ProductDAO;
import com.balajimart.model.CartItem;
import com.balajimart.model.Order;
import com.balajimart.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceTest {

    private OrderService orderService;
    private StubCartDAO cartDAO;
    private StubProductDAO productDAO;
    private StubOrderDAO orderDAO;

    private static class StubCartDAO extends CartDAO {
        private final List<CartItem> cartItems = new ArrayList<>();

        public void addItem(CartItem item) {
            cartItems.add(item);
        }

        @Override
        public List<CartItem> findByUserId(Long userId) {
            return cartItems;
        }
    }

    private static class StubProductDAO extends ProductDAO {
        private Product product;

        public void setProduct(Product product) {
            this.product = product;
        }

        @Override
        public Product findById(Long id) {
            return product;
        }
    }

    private static class StubOrderDAO extends OrderDAO {
        private final List<Order> userOrders = new ArrayList<>();

        @Override
        public List<Order> findOrdersByUser(Long userId) {
            return userOrders;
        }
    }

    @BeforeEach
    void setUp() {
        cartDAO = new StubCartDAO();
        productDAO = new StubProductDAO();
        orderDAO = new StubOrderDAO();
        orderService = new OrderService(orderDAO, cartDAO, productDAO);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when placing order with empty cart")
    void testPlaceOrderEmptyCart() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                orderService.placeOrder(1L)
        );
        assertEquals("Your shopping cart is empty", ex.getMessage());
    }

    @Test
    @DisplayName("Should return order list for a user")
    void testGetUserOrders() {
        List<Order> orders = orderService.getUserOrders(1L);
        assertNotNull(orders);
        assertTrue(orders.isEmpty());
    }
}
