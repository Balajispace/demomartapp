package com.balajimart.controller;

import com.balajimart.model.Product;
import com.balajimart.service.ProductService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProductServlet", urlPatterns = {"/products", "/product"})
public class ProductServlet extends HttpServlet {

    private ProductService productService;

    @Override
    public void init() throws ServletException {
        this.productService = new ProductService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String servletPath = request.getServletPath();
        String productIdParam = request.getParameter("id");

        if ("/product".equals(servletPath) || (productIdParam != null && !productIdParam.trim().isEmpty())) {
            try {
                Long productId = Long.parseLong(productIdParam);
                Product product = productService.getProductById(productId);
                if (product == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
                    return;
                }
                request.setAttribute("product", product);
                request.getRequestDispatcher("/WEB-INF/jsp/product-details.jsp").forward(request, response);
                return;
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Product ID");
                return;
            }
        }

        String query = request.getParameter("q");
        String category = request.getParameter("category");

        List<Product> products;
        if (query != null && !query.trim().isEmpty()) {
            products = productService.searchProducts(query);
            request.setAttribute("searchQuery", query);
        } else if (category != null && !category.trim().isEmpty()) {
            products = productService.getProductsByCategory(category);
            request.setAttribute("selectedCategory", category);
        } else {
            products = productService.getAllProducts();
        }

        request.setAttribute("products", products);
        request.getRequestDispatcher("/WEB-INF/jsp/products.jsp").forward(request, response);
    }
}
