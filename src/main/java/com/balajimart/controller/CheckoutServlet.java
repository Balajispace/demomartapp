package com.balajimart.controller;

import com.balajimart.model.CartItem;
import com.balajimart.model.Order;
import com.balajimart.model.User;
import com.balajimart.service.CartService;
import com.balajimart.service.OrderService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet(name = "CheckoutServlet", urlPatterns = {"/checkout"})
public class CheckoutServlet extends HttpServlet {

    private CartService cartService;
    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        this.cartService = new CartService();
        this.orderService = new OrderService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = getLoggedInUser(request);
        List<CartItem> cartItems = cartService.getUserCart(user.getId());

        if (cartItems == null || cartItems.isEmpty()) {
            HttpSession session = request.getSession();
            session.setAttribute("errorMessage", "Your cart is empty. Please add items before checking out.");
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        BigDecimal cartTotal = cartService.calculateTotal(cartItems);

        request.setAttribute("cartItems", cartItems);
        request.setAttribute("cartTotal", cartTotal);
        request.getRequestDispatcher("/WEB-INF/jsp/checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = getLoggedInUser(request);
        HttpSession session = request.getSession();

        try {
            Order order = orderService.placeOrder(user.getId());
            session.setAttribute("successMessage", "Order #" + order.getId() + " placed successfully! Thank you for shopping with BALAJIMART.");
            response.sendRedirect(request.getContextPath() + "/order?id=" + order.getId());
        } catch (IllegalArgumentException | IllegalStateException e) {
            session.setAttribute("errorMessage", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/checkout");
        } catch (Exception e) {
            session.setAttribute("errorMessage", "Failed to place order: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/checkout");
        }
    }

    private User getLoggedInUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (User) session.getAttribute("user");
    }
}
