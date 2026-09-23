package com.balajimart.controller;

import com.balajimart.model.Product;
import com.balajimart.model.User;
import com.balajimart.service.ProductService;
import com.balajimart.service.WishlistService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@WebServlet(name = "HomeServlet", urlPatterns = {"", "/home"})
public class HomeServlet extends HttpServlet {

    private ProductService productService;
    private WishlistService wishlistService;

    @Override
    public void init() throws ServletException {
        this.productService = new ProductService();
        this.wishlistService = new WishlistService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user != null) {
            Set<Long> wishlistedIds = wishlistService.getWishlistedProductIds(user.getId());
            request.setAttribute("wishlistProductIds", wishlistedIds);
        }

        List<Product> products = productService.getAllProducts();
        List<Product> featuredProducts = products.size() > 6 ? products.subList(0, 6) : products;

        request.setAttribute("featuredProducts", featuredProducts);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}
