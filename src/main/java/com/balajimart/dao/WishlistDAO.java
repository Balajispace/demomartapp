package com.balajimart.dao;

import com.balajimart.config.DatabaseConfig;
import com.balajimart.model.Product;
import com.balajimart.model.Wishlist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WishlistDAO {

    public List<Wishlist> findByUserId(Long userId) {
        List<Wishlist> list = new ArrayList<>();
        String sql = "SELECT w.id AS w_id, w.user_id, w.product_id, w.created_at AS w_created, " +
                     "       p.id AS p_id, p.name AS p_name, p.description AS p_desc, p.price AS p_price, " +
                     "       p.category AS p_cat, p.stock AS p_stock, p.image_url AS p_img, " +
                     "       p.seller_id, u.name AS seller_name, p.status AS p_status " +
                     "FROM wishlist w " +
                     "JOIN products p ON w.product_id = p.id " +
                     "LEFT JOIN users u ON p.seller_id = u.id " +
                     "WHERE w.user_id = ? " +
                     "ORDER BY w.id DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Wishlist item = new Wishlist();
                    item.setId(rs.getLong("w_id"));
                    item.setUserId(rs.getLong("user_id"));
                    item.setProductId(rs.getLong("product_id"));
                    item.setCreatedAt(rs.getTimestamp("w_created"));

                    Product p = new Product();
                    p.setId(rs.getLong("p_id"));
                    p.setName(rs.getString("p_name"));
                    p.setDescription(rs.getString("p_desc"));
                    p.setPrice(rs.getBigDecimal("p_price"));
                    p.setCategory(rs.getString("p_cat"));
                    p.setStock(rs.getInt("p_stock"));
                    p.setImageUrl(rs.getString("p_img"));
                    p.setSellerId(rs.getObject("seller_id") != null ? rs.getLong("seller_id") : null);
                    p.setSellerName(rs.getString("seller_name"));
                    p.setStatus(rs.getString("p_status"));

                    item.setProduct(p);
                    list.add(item);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error fetching wishlist items", e);
        }
        return list;
    }

    public boolean addWishlistItem(Long userId, Long productId) {
        if (isWishlisted(userId, productId)) {
            return true; // Already wishlisted, ignore duplicate
        }
        String sql = "INSERT INTO wishlist (user_id, product_id) VALUES (?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setLong(2, productId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error adding wishlist item", e);
        }
    }

    public boolean removeWishlistItem(Long userId, Long productId) {
        String sql = "DELETE FROM wishlist WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setLong(2, productId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error removing wishlist item", e);
        }
    }

    public boolean isWishlisted(Long userId, Long productId) {
        String sql = "SELECT COUNT(*) FROM wishlist WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setLong(2, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error checking wishlist state", e);
        }
        return false;
    }

    public Set<Long> getWishlistedProductIds(Long userId) {
        Set<Long> set = new HashSet<>();
        if (userId == null) return set;
        String sql = "SELECT product_id FROM wishlist WHERE user_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    set.add(rs.getLong("product_id"));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error fetching wishlisted product IDs", e);
        }
        return set;
    }
}
