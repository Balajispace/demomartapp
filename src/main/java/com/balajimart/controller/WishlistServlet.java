package com.balajimart.controller;

import com.balajimart.model.User;
import com.balajimart.model.Wishlist;
import com.balajimart.service.WishlistService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "WishlistServlet", urlPatterns = {"/wishlist", "/wishlist/add", "/wishlist/remove", "/wishlist/toggle"})
public class WishlistServlet extends HttpServlet {

    private WishlistService wishlistService;

    @Override
    public void init() throws ServletException {
        this.wishlistService = new WishlistService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = getLoggedInUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        List<Wishlist> wishlistItems = wishlistService.getUserWishlist(user.getId());
        request.setAttribute("wishlistItems", wishlistItems);
        request.getRequestDispatcher("/WEB-INF/jsp/wishlist.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = getLoggedInUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String path = request.getServletPath();
        HttpSession session = request.getSession();

        try {
            String productIdParam = request.getParameter("productId");
            if (productIdParam == null || productIdParam.trim().isEmpty()) {
                throw new IllegalArgumentException("Product ID is required.");
            }
            Long productId = Long.parseLong(productIdParam);

            if ("/wishlist/toggle".equals(path)) {
                boolean added = wishlistService.toggleWishlist(user.getId(), productId);
                if (added) {
                    session.setAttribute("successMessage", "Product added to your wishlist!");
                } else {
                    session.setAttribute("successMessage", "Product removed from your wishlist.");
                }
            } else if ("/wishlist/add".equals(path)) {
                wishlistService.addToWishlist(user.getId(), productId);
                session.setAttribute("successMessage", "Product added to your wishlist!");
            } else if ("/wishlist/remove".equals(path)) {
                wishlistService.removeFromWishlist(user.getId(), productId);
                session.setAttribute("successMessage", "Product removed from your wishlist.");
            }

            String redirect = request.getParameter("redirect");
            if ("product".equals(redirect)) {
                response.sendRedirect(request.getContextPath() + "/product?id=" + productId);
            } else if ("catalog".equals(redirect)) {
                response.sendRedirect(request.getContextPath() + "/products");
            } else if ("home".equals(redirect)) {
                response.sendRedirect(request.getContextPath() + "/home");
            } else {
                response.sendRedirect(request.getContextPath() + "/wishlist");
            }
        } catch (IllegalArgumentException e) {
            session.setAttribute("errorMessage", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/wishlist");
        } catch (Exception e) {
            session.setAttribute("errorMessage", "An error occurred while updating wishlist.");
            response.sendRedirect(request.getContextPath() + "/wishlist");
        }
    }

    private User getLoggedInUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (session != null) ? (User) session.getAttribute("user") : null;
    }
}
