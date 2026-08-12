package com.balajimart.controller;

import com.balajimart.model.Order;
import com.balajimart.model.User;
import com.balajimart.service.OrderService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "OrderServlet", urlPatterns = {"/orders", "/order"})
public class OrderServlet extends HttpServlet {

    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        this.orderService = new OrderService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = getLoggedInUser(request);
        String servletPath = request.getServletPath();
        String orderIdParam = request.getParameter("id");

        if ("/order".equals(servletPath) || (orderIdParam != null && !orderIdParam.trim().isEmpty())) {
            try {
                Long orderId = Long.parseLong(orderIdParam);
                Order order = orderService.getOrderDetails(orderId, user.getId());
                if (order == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found");
                    return;
                }
                request.setAttribute("order", order);
                request.getRequestDispatcher("/WEB-INF/jsp/order-details.jsp").forward(request, response);
                return;
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Order ID");
                return;
            }
        }

        List<Order> orders = orderService.getUserOrders(user.getId());
        request.setAttribute("orders", orders);
        request.getRequestDispatcher("/WEB-INF/jsp/orders.jsp").forward(request, response);
    }

    private User getLoggedInUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (User) session.getAttribute("user");
    }
}
