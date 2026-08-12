package com.balajimart.controller;

import com.balajimart.model.CartItem;
import com.balajimart.model.User;
import com.balajimart.service.CartService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet(name = "CartServlet", urlPatterns = {"/cart", "/cart/add", "/cart/update", "/cart/remove"})
public class CartServlet extends HttpServlet {

    private CartService cartService;

    @Override
    public void init() throws ServletException {
        this.cartService = new CartService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = getLoggedInUser(request);
        List<CartItem> cartItems = cartService.getUserCart(user.getId());
        BigDecimal cartTotal = cartService.calculateTotal(cartItems);

        request.setAttribute("cartItems", cartItems);
        request.setAttribute("cartTotal", cartTotal);
        request.getRequestDispatcher("/WEB-INF/jsp/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = getLoggedInUser(request);
        String path = request.getServletPath();
        HttpSession session = request.getSession();

        try {
            if ("/cart/add".equals(path)) {
                Long productId = Long.parseLong(request.getParameter("productId"));
                int quantity = 1;
                String qtyParam = request.getParameter("quantity");
                if (qtyParam != null && !qtyParam.trim().isEmpty()) {
                    quantity = Integer.parseInt(qtyParam);
                }

                cartService.addToCart(user.getId(), productId, quantity);
                session.setAttribute("successMessage", "Item added to cart successfully!");
                
                String redirect = request.getParameter("redirect");
                if ("product".equals(redirect)) {
                    response.sendRedirect(request.getContextPath() + "/product?id=" + productId);
                } else {
                    response.sendRedirect(request.getContextPath() + "/cart");
                }
                return;

            } else if ("/cart/update".equals(path)) {
                Long cartItemId = Long.parseLong(request.getParameter("cartItemId"));
                int quantity = Integer.parseInt(request.getParameter("quantity"));

                cartService.updateCartQuantity(cartItemId, user.getId(), quantity);
                session.setAttribute("successMessage", "Cart updated successfully.");
                response.sendRedirect(request.getContextPath() + "/cart");
                return;

            } else if ("/cart/remove".equals(path)) {
                Long cartItemId = Long.parseLong(request.getParameter("cartItemId"));
                cartService.removeFromCart(cartItemId, user.getId());
                session.setAttribute("successMessage", "Item removed from cart.");
                response.sendRedirect(request.getContextPath() + "/cart");
                return;
            }
        } catch (IllegalArgumentException e) {
            session.setAttribute("errorMessage", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        } catch (Exception e) {
            session.setAttribute("errorMessage", "An unexpected error occurred: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        response.sendRedirect(request.getContextPath() + "/cart");
    }

    private User getLoggedInUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (User) session.getAttribute("user");
    }
}
