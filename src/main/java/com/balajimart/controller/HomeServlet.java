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

@WebServlet(name = "HomeServlet", urlPatterns = {"", "/home"})
public class HomeServlet extends HttpServlet {

    private ProductService productService;

    @Override
    public void init() throws ServletException {
        this.productService = new ProductService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Product> products = productService.getAllProducts();
        // Limit to top 6 featured products for homepage
        List<Product> featuredProducts = products.size() > 6 ? products.subList(0, 6) : products;

        request.setAttribute("featuredProducts", featuredProducts);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}
