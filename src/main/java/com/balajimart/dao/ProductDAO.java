package com.balajimart.dao;

import com.balajimart.config.DatabaseConfig;
import com.balajimart.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT id, name, description, price, category, stock, image_url, created_at FROM products ORDER BY id ASC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                products.add(mapProduct(rs));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error fetching products", e);
        }
        return products;
    }

    public Product findById(Long id) {
        String sql = "SELECT id, name, description, price, category, stock, image_url, created_at FROM products WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapProduct(rs);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error fetching product by id", e);
        }
        return null;
    }

    public List<Product> searchByName(String query) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT id, name, description, price, category, stock, image_url, created_at FROM products WHERE LOWER(name) LIKE ? OR LOWER(category) LIKE ? ORDER BY id ASC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String pattern = "%" + query.trim().toLowerCase() + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(mapProduct(rs));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error searching products", e);
        }
        return products;
    }

    public List<Product> findByCategory(String category) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT id, name, description, price, category, stock, image_url, created_at FROM products WHERE LOWER(category) = ? ORDER BY id ASC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, category.trim().toLowerCase());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(mapProduct(rs));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error fetching products by category", e);
        }
        return products;
    }

    public boolean reduceStock(Connection conn, Long productId, int quantity) throws Exception {
        String sql = "UPDATE products SET stock = stock - ? WHERE id = ? AND stock >= ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setLong(2, productId);
            ps.setInt(3, quantity);
            int rows = ps.executeUpdate();
            return rows > 0;
        }
    }

    private Product mapProduct(ResultSet rs) throws Exception {
        Product p = new Product();
        p.setId(rs.getLong("id"));
        p.setName(rs.getString("name"));
        p.setDescription(rs.getString("description"));
        p.setPrice(rs.getBigDecimal("price"));
        p.setCategory(rs.getString("category"));
        p.setStock(rs.getInt("stock"));
        p.setImageUrl(rs.getString("image_url"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        return p;
    }
}
