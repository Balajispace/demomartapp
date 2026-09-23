package com.balajimart.dao;

import com.balajimart.config.DatabaseConfig;
import com.balajimart.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    private static final String BASE_SELECT = 
            "SELECT p.id, p.name, p.description, p.price, p.category, p.stock, p.image_url, " +
            "       p.seller_id, u.name AS seller_name, p.status, p.created_at " +
            "FROM products p " +
            "LEFT JOIN users u ON p.seller_id = u.id ";

    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE p.status = 'APPROVED' ORDER BY p.id ASC";
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

    public List<Product> findAllForAdmin() {
        List<Product> products = new ArrayList<>();
        String sql = BASE_SELECT + "ORDER BY p.id DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                products.add(mapProduct(rs));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error fetching all products for admin", e);
        }
        return products;
    }

    public List<Product> findBySellerId(Long sellerId) {
        List<Product> products = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE p.seller_id = ? ORDER BY p.id DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, sellerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(mapProduct(rs));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error fetching seller products", e);
        }
        return products;
    }

    public Product findById(Long id) {
        String sql = BASE_SELECT + "WHERE p.id = ?";
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
        String sql = BASE_SELECT + "WHERE p.status = 'APPROVED' AND (LOWER(p.name) LIKE ? OR LOWER(p.category) LIKE ?) ORDER BY p.id ASC";
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
        String sql = BASE_SELECT + "WHERE p.status = 'APPROVED' AND LOWER(p.category) = ? ORDER BY p.id ASC";
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

    public Product createProduct(Product product) {
        String sql = "INSERT INTO products (name, description, price, category, stock, image_url, seller_id, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, product.getName().trim());
            ps.setString(2, product.getDescription() != null ? product.getDescription().trim() : "");
            ps.setBigDecimal(3, product.getPrice());
            ps.setString(4, product.getCategory() != null ? product.getCategory().trim() : "General");
            ps.setInt(5, product.getStock());
            ps.setString(6, product.getImageUrl() != null ? product.getImageUrl().trim() : "");
            if (product.getSellerId() != null) {
                ps.setLong(7, product.getSellerId());
            } else {
                ps.setNull(7, java.sql.Types.BIGINT);
            }
            ps.setString(8, product.getStatus() != null ? product.getStatus().toUpperCase() : "APPROVED");

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        product.setId(keys.getLong(1));
                    }
                }
                return product;
            }
        } catch (Exception e) {
            throw new RuntimeException("Error creating product", e);
        }
        return null;
    }

    public boolean updateProduct(Product product) {
        String sql = "UPDATE products SET name = ?, description = ?, price = ?, category = ?, stock = ?, image_url = ? WHERE id = ?" +
                (product.getSellerId() != null ? " AND seller_id = ?" : "");
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, product.getName().trim());
            ps.setString(2, product.getDescription() != null ? product.getDescription().trim() : "");
            ps.setBigDecimal(3, product.getPrice());
            ps.setString(4, product.getCategory() != null ? product.getCategory().trim() : "General");
            ps.setInt(5, product.getStock());
            ps.setString(6, product.getImageUrl() != null ? product.getImageUrl().trim() : "");
            ps.setLong(7, product.getId());
            if (product.getSellerId() != null) {
                ps.setLong(8, product.getSellerId());
            }
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error updating product", e);
        }
    }

    public boolean deleteProduct(Long id, Long sellerId) {
        String sql = "DELETE FROM products WHERE id = ?" + (sellerId != null ? " AND seller_id = ?" : "");
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            if (sellerId != null) {
                ps.setLong(2, sellerId);
            }
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error deleting product", e);
        }
    }

    public boolean updateProductStatus(Long id, String status) {
        String sql = "UPDATE products SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.trim().toUpperCase());
            ps.setLong(2, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error updating product status", e);
        }
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

    public int countProducts(String status) {
        String sql = (status == null || status.trim().isEmpty()) ?
                "SELECT COUNT(*) FROM products" :
                "SELECT COUNT(*) FROM products WHERE UPPER(status) = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (status != null && !status.trim().isEmpty()) {
                ps.setString(1, status.trim().toUpperCase());
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error counting products", e);
        }
        return 0;
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
        p.setSellerId(rs.getObject("seller_id") != null ? rs.getLong("seller_id") : null);
        p.setSellerName(rs.getString("seller_name"));
        p.setStatus(rs.getString("status"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        return p;
    }
}
