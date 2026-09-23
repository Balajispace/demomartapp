package com.balajimart.service;

import com.balajimart.dao.ProductDAO;
import com.balajimart.model.Product;
import com.balajimart.util.ValidationUtil;

import java.math.BigDecimal;
import java.util.List;

public class ProductService {

    private final ProductDAO productDAO;

    public ProductService() {
        this.productDAO = new ProductDAO();
    }

    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public List<Product> getAllProducts() {
        return productDAO.findAll();
    }

    public List<Product> getAllProductsForAdmin() {
        return productDAO.findAllForAdmin();
    }

    public List<Product> getProductsBySeller(Long sellerId) {
        if (sellerId == null) return List.of();
        return productDAO.findBySellerId(sellerId);
    }

    public Product getProductById(Long id) {
        if (id == null || id <= 0) {
            return null;
        }
        return productDAO.findById(id);
    }

    public List<Product> searchProducts(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllProducts();
        }
        return productDAO.searchByName(query);
    }

    public List<Product> getProductsByCategory(String category) {
        if (category == null || category.trim().isEmpty() || "All".equalsIgnoreCase(category)) {
            return getAllProducts();
        }
        return productDAO.findByCategory(category);
    }

    public Product addProduct(Product product) throws IllegalArgumentException {
        validateProduct(product);
        if (product.getStatus() == null || product.getStatus().trim().isEmpty()) {
            product.setStatus("APPROVED");
        }
        return productDAO.createProduct(product);
    }

    public boolean updateProduct(Product product) throws IllegalArgumentException {
        validateProduct(product);
        if (product.getId() == null || product.getId() <= 0) {
            throw new IllegalArgumentException("Invalid product ID");
        }
        return productDAO.updateProduct(product);
    }

    public boolean deleteProduct(Long productId, Long sellerId) throws IllegalArgumentException {
        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("Invalid product ID");
        }
        return productDAO.deleteProduct(productId, sellerId);
    }

    public boolean updateProductStatus(Long productId, String status) throws IllegalArgumentException {
        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("Invalid product ID");
        }
        if (!ValidationUtil.isNotEmpty(status)) {
            throw new IllegalArgumentException("Status cannot be empty");
        }
        String s = status.trim().toUpperCase();
        if (!"APPROVED".equals(s) && !"REJECTED".equals(s) && !"PENDING".equals(s)) {
            throw new IllegalArgumentException("Invalid status value");
        }
        return productDAO.updateProductStatus(productId, s);
    }

    private void validateProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product data cannot be null");
        }
        if (!ValidationUtil.isNotEmpty(product.getName())) {
            throw new IllegalArgumentException("Product name is required");
        }
        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Product price must be a valid non-negative number");
        }
        if (product.getStock() < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }
        if (!ValidationUtil.isNotEmpty(product.getCategory())) {
            throw new IllegalArgumentException("Product category is required");
        }
    }
}
