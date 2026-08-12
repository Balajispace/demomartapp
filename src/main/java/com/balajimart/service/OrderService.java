package com.balajimart.service;

import com.balajimart.config.DatabaseConfig;
import com.balajimart.dao.CartDAO;
import com.balajimart.dao.OrderDAO;
import com.balajimart.dao.ProductDAO;
import com.balajimart.model.CartItem;
import com.balajimart.model.Order;
import com.balajimart.model.OrderItem;
import com.balajimart.model.Product;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class OrderService {

    private final OrderDAO orderDAO;
    private final CartDAO cartDAO;
    private final ProductDAO productDAO;

    public OrderService() {
        this.orderDAO = new OrderDAO();
        this.cartDAO = new CartDAO();
        this.productDAO = new ProductDAO();
    }

    public OrderService(OrderDAO orderDAO, CartDAO cartDAO, ProductDAO productDAO) {
        this.orderDAO = orderDAO;
        this.cartDAO = cartDAO;
        this.productDAO = productDAO;
    }

    public Order placeOrder(Long userId) throws Exception {
        List<CartItem> cartItems = cartDAO.findByUserId(userId);
        if (cartItems == null || cartItems.isEmpty()) {
            throw new IllegalArgumentException("Your shopping cart is empty");
        }

        Connection conn = null;
        try {
            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false); // Begin transaction

            BigDecimal totalAmount = BigDecimal.ZERO;

            // Step 1 & 2: Check stock & calculate total
            for (CartItem item : cartItems) {
                Product product = productDAO.findById(item.getProductId());
                if (product == null) {
                    throw new IllegalStateException("Product '" + item.getProduct().getName() + "' no longer exists");
                }
                if (product.getStock() < item.getQuantity()) {
                    throw new IllegalStateException("Insufficient stock for product '" + product.getName() + "'. Available: " + product.getStock());
                }
                BigDecimal lineSubtotal = product.getPrice().multiply(new BigDecimal(item.getQuantity()));
                totalAmount = totalAmount.add(lineSubtotal);
            }

            // Step 3 & 4: Create Order record
            Order order = new Order();
            order.setUserId(userId);
            order.setTotalAmount(totalAmount);
            order.setStatus("PENDING");

            Long orderId = orderDAO.createOrder(conn, order);
            order.setId(orderId);

            // Step 5 & 6: Create Order items and reduce product stock
            for (CartItem item : cartItems) {
                Product product = productDAO.findById(item.getProductId());

                OrderItem orderItem = new OrderItem();
                orderItem.setOrderId(orderId);
                orderItem.setProductId(item.getProductId());
                orderItem.setQuantity(item.getQuantity());
                orderItem.setUnitPrice(product.getPrice());
                orderItem.setProductName(product.getName());
                orderItem.setProductImage(product.getImageUrl());

                orderDAO.createOrderItem(conn, orderItem);
                order.getItems().add(orderItem);

                boolean stockDeducted = productDAO.reduceStock(conn, item.getProductId(), item.getQuantity());
                if (!stockDeducted) {
                    throw new IllegalStateException("Failed to update stock for product: " + product.getName());
                }
            }

            // Step 7: Clear user cart
            cartDAO.clearCart(conn, userId);

            // Step 8: Commit transaction
            conn.commit();
            return order;

        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback on failure
                } catch (SQLException ex) {
                    // ignore rollback exception
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ex) {
                    // ignore close exception
                }
            }
        }
    }

    public List<Order> getUserOrders(Long userId) {
        return orderDAO.findOrdersByUser(userId);
    }

    public Order getOrderDetails(Long orderId, Long userId) {
        return orderDAO.findOrderById(orderId, userId);
    }
}
