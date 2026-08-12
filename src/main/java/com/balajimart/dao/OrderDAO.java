package com.balajimart.dao;

import com.balajimart.config.DatabaseConfig;
import com.balajimart.model.Order;
import com.balajimart.model.OrderItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    public Long createOrder(Connection conn, Order order) throws Exception {
        String sql = "INSERT INTO orders (user_id, total_amount, status) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, order.getUserId());
            ps.setBigDecimal(2, order.getTotalAmount());
            ps.setString(3, order.getStatus() != null ? order.getStatus() : "PENDING");
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    long id = keys.getLong(1);
                    order.setId(id);
                    return id;
                }
            }
        }
        throw new Exception("Failed to insert order record");
    }

    public void createOrderItem(Connection conn, OrderItem item) throws Exception {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, item.getOrderId());
            ps.setLong(2, item.getProductId());
            ps.setInt(3, item.getQuantity());
            ps.setBigDecimal(4, item.getUnitPrice());
            ps.executeUpdate();
        }
    }

    public List<Order> findOrdersByUser(Long userId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT id, user_id, total_amount, status, created_at FROM orders WHERE user_id = ? ORDER BY created_at DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = mapOrder(rs);
                    orders.add(order);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error fetching orders for user", e);
        }
        return orders;
    }

    public Order findOrderById(Long orderId, Long userId) {
        String sql = "SELECT id, user_id, total_amount, status, created_at FROM orders WHERE id = ? AND user_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            ps.setLong(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Order order = mapOrder(rs);
                    order.setItems(findItemsByOrderId(conn, orderId));
                    return order;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error fetching order details", e);
        }
        return null;
    }

    private List<OrderItem> findItemsByOrderId(Connection conn, Long orderId) throws Exception {
        List<OrderItem> items = new ArrayList<>();
        String sql = """
            SELECT oi.id, oi.order_id, oi.product_id, oi.quantity, oi.unit_price,
                   p.name AS product_name, p.image_url AS product_image
            FROM order_items oi
            JOIN products p ON oi.product_id = p.id
            WHERE oi.order_id = ?
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderItem item = new OrderItem();
                    item.setId(rs.getLong("id"));
                    item.setOrderId(rs.getLong("order_id"));
                    item.setProductId(rs.getLong("product_id"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setUnitPrice(rs.getBigDecimal("unit_price"));
                    item.setProductName(rs.getString("product_name"));
                    item.setProductImage(rs.getString("product_image"));
                    items.add(item);
                }
            }
        }
        return items;
    }

    private Order mapOrder(ResultSet rs) throws Exception {
        Order order = new Order();
        order.setId(rs.getLong("id"));
        order.setUserId(rs.getLong("user_id"));
        order.setTotalAmount(rs.getBigDecimal("total_amount"));
        order.setStatus(rs.getString("status"));
        order.setCreatedAt(rs.getTimestamp("created_at"));
        return order;
    }
}
