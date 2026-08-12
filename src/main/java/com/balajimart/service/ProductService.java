package com.balajimart.service;

import com.balajimart.dao.ProductDAO;
import com.balajimart.model.Product;

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
}
