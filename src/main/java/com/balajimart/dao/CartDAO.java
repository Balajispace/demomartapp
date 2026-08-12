package com.balajimart.dao;

import com.balajimart.config.DatabaseConfig;
import com.balajimart.model.CartItem;
import com.balajimart.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {

    public List<CartItem> findByUserId(Long userId) {
        List<CartItem> items = new ArrayList<>();
        String sql = """
            SELECT c.id AS cart_id, c.user_id, c.product_id, c.quantity,
                   p.id AS p_id, p.name AS p_name, p.description AS p_desc, p.price AS p_price,
                   p.category AS p_cat, p.stock AS p_stock, p.image_url AS p_img
            FROM cart_items c
            JOIN products p ON c.product_id = p.id
            WHERE c.user_id = ?
            ORDER BY c.id ASC
        """;
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CartItem item = new CartItem();
                    item.setId(rs.getLong("cart_id"));
                    item.setUserId(rs.getLong("user_id"));
                    item.setProductId(rs.getLong("product_id"));
                    item.setQuantity(rs.getInt("quantity"));

                    Product p = new Product();
                    p.setId(rs.getLong("p_id"));
                    p.setName(rs.getString("p_name"));
                    p.setDescription(rs.getString("p_desc"));
                    p.setPrice(rs.getBigDecimal("p_price"));
                    p.setCategory(rs.getString("p_cat"));
                    p.setStock(rs.getInt("p_stock"));
                    p.setImageUrl(rs.getString("p_img"));

                    item.setProduct(p);
                    items.add(item);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error fetching cart items for user", e);
        }
        return items;
    }

    public CartItem findItem(Long userId, Long productId) {
        String sql = "SELECT id, user_id, product_id, quantity FROM cart_items WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setLong(2, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    CartItem item = new CartItem();
                    item.setId(rs.getLong("id"));
                    item.setUserId(rs.getLong("user_id"));
                    item.setProductId(rs.getLong("product_id"));
                    item.setQuantity(rs.getInt("quantity"));
                    return item;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error finding cart item", e);
        }
        return null;
    }

    public boolean addItem(Long userId, Long productId, int quantity) {
        String sql = "INSERT INTO cart_items (user_id, product_id, quantity) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setLong(2, productId);
            ps.setInt(3, quantity);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error adding item to cart", e);
        }
    }

    public boolean updateQuantity(Long cartItemId, Long userId, int newQuantity) {
        String sql = "UPDATE cart_items SET quantity = ? WHERE id = ? AND user_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newQuantity);
            ps.setLong(2, cartItemId);
            ps.setLong(3, userId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error updating cart quantity", e);
        }
    }

    public boolean removeItem(Long cartItemId, Long userId) {
        String sql = "DELETE FROM cart_items WHERE id = ? AND user_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, cartItemId);
            ps.setLong(2, userId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error removing item from cart", e);
        }
    }

    public boolean clearCart(Connection conn, Long userId) throws Exception {
        String sql = "DELETE FROM cart_items WHERE user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            return ps.executeUpdate() >= 0;
        }
    }

    public boolean clearCart(Long userId) {
        try (Connection conn = DatabaseConfig.getConnection()) {
            return clearCart(conn, userId);
        } catch (Exception e) {
            throw new RuntimeException("Error clearing cart", e);
        }
    }
}
