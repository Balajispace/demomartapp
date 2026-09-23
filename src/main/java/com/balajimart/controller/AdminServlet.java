package com.balajimart.controller;

import com.balajimart.dao.OrderDAO;
import com.balajimart.dao.ProductDAO;
import com.balajimart.dao.UserDAO;
import com.balajimart.model.Order;
import com.balajimart.model.Product;
import com.balajimart.model.User;
import com.balajimart.service.ProductService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet(name = "AdminServlet", urlPatterns = {
        "/admin/dashboard",
        "/admin/users",
        "/admin/users/status",
        "/admin/products",
        "/admin/products/status",
        "/admin/products/delete",
        "/admin/orders"
})
public class AdminServlet extends HttpServlet {

    private UserDAO userDAO;
    private ProductDAO productDAO;
    private ProductService productService;
    private OrderDAO orderDAO;

    @Override
    public void init() throws ServletException {
        this.userDAO = new UserDAO();
        this.productDAO = new ProductDAO();
        this.productService = new ProductService();
        this.orderDAO = new OrderDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        if ("/admin/dashboard".equals(path)) {
            int totalUsers = userDAO.countUsers(null);
            int totalBuyers = userDAO.countUsers("BUYER");
            int totalSellers = userDAO.countUsers("SELLER");
            int totalProducts = productDAO.countProducts(null);
            int pendingProducts = productDAO.countProducts("PENDING");
            int totalOrders = orderDAO.countOrders();
            BigDecimal totalRevenue = orderDAO.calculateTotalRevenue();

            request.setAttribute("totalUsers", totalUsers);
            request.setAttribute("totalBuyers", totalBuyers);
            request.setAttribute("totalSellers", totalSellers);
            request.setAttribute("totalProducts", totalProducts);
            request.setAttribute("pendingProducts", pendingProducts);
            request.setAttribute("totalOrders", totalOrders);
            request.setAttribute("totalRevenue", totalRevenue);

            List<Product> recentProducts = productService.getAllProductsForAdmin();
            if (recentProducts.size() > 5) {
                recentProducts = recentProducts.subList(0, 5);
            }
            request.setAttribute("recentProducts", recentProducts);

            request.getRequestDispatcher("/WEB-INF/jsp/admin/dashboard.jsp").forward(request, response);

        } else if ("/admin/users".equals(path)) {
            String roleFilter = request.getParameter("role");
            List<User> users;
            if (roleFilter != null && !roleFilter.trim().isEmpty() && !"ALL".equalsIgnoreCase(roleFilter)) {
                users = userDAO.findByRole(roleFilter);
                request.setAttribute("selectedRole", roleFilter.toUpperCase());
            } else {
                users = userDAO.findAll();
                request.setAttribute("selectedRole", "ALL");
            }
            request.setAttribute("users", users);
            request.getRequestDispatcher("/WEB-INF/jsp/admin/users.jsp").forward(request, response);

        } else if ("/admin/products".equals(path)) {
            List<Product> products = productService.getAllProductsForAdmin();
            request.setAttribute("products", products);
            request.getRequestDispatcher("/WEB-INF/jsp/admin/products.jsp").forward(request, response);

        } else if ("/admin/orders".equals(path)) {
            List<Order> orders = orderDAO.findAllOrders();
            request.setAttribute("orders", orders);
            request.getRequestDispatcher("/WEB-INF/jsp/admin/orders.jsp").forward(request, response);

        } else {
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();
        HttpSession session = request.getSession();

        try {
            if ("/admin/users/status".equals(path)) {
                Long userId = Long.parseLong(request.getParameter("userId"));
                String status = request.getParameter("status");

                // Prevent admin from disabling themselves
                User currentUser = (User) session.getAttribute("user");
                if (currentUser.getId().equals(userId) && "DISABLED".equalsIgnoreCase(status)) {
                    session.setAttribute("errorMessage", "You cannot disable your own admin account!");
                } else {
                    userDAO.updateUserStatus(userId, status);
                    session.setAttribute("successMessage", "User status updated to " + status.toUpperCase());
                }
                response.sendRedirect(request.getContextPath() + "/admin/users");
                return;

            } else if ("/admin/products/status".equals(path)) {
                Long productId = Long.parseLong(request.getParameter("productId"));
                String status = request.getParameter("status");

                productService.updateProductStatus(productId, status);
                session.setAttribute("successMessage", "Product status updated to " + status.toUpperCase());
                response.sendRedirect(request.getContextPath() + "/admin/products");
                return;

            } else if ("/admin/products/delete".equals(path)) {
                Long productId = Long.parseLong(request.getParameter("productId"));
                productService.deleteProduct(productId, null); // Admin can delete any product
                session.setAttribute("successMessage", "Product deleted successfully.");
                response.sendRedirect(request.getContextPath() + "/admin/products");
                return;
            }
        } catch (Exception e) {
            session.setAttribute("errorMessage", "Operation failed: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            return;
        }

        response.sendRedirect(request.getContextPath() + "/admin/dashboard");
    }
}
