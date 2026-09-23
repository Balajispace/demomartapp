package com.balajimart.controller;

import com.balajimart.dao.OrderDAO;
import com.balajimart.model.OrderItem;
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

@WebServlet(name = "SellerServlet", urlPatterns = {
        "/seller/dashboard",
        "/seller/products",
        "/seller/products/add",
        "/seller/products/edit",
        "/seller/products/delete",
        "/seller/orders"
})
public class SellerServlet extends HttpServlet {

    private ProductService productService;
    private OrderDAO orderDAO;

    @Override
    public void init() throws ServletException {
        this.productService = new ProductService();
        this.orderDAO = new OrderDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User seller = getLoggedInUser(request);
        String path = request.getServletPath();

        if ("/seller/dashboard".equals(path)) {
            List<Product> products = productService.getProductsBySeller(seller.getId());
            long totalProducts = products.size();
            long activeProducts = products.stream().filter(p -> "APPROVED".equalsIgnoreCase(p.getStatus())).count();
            long pendingProducts = products.stream().filter(p -> "PENDING".equalsIgnoreCase(p.getStatus())).count();

            List<OrderItem> salesItems = orderDAO.findOrderItemsBySeller(seller.getId());
            int totalItemsSold = salesItems.stream().mapToInt(OrderItem::getQuantity).sum();
            BigDecimal totalEarnings = salesItems.stream()
                    .map(item -> item.getUnitPrice().multiply(new BigDecimal(item.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            request.setAttribute("totalProducts", totalProducts);
            request.setAttribute("activeProducts", activeProducts);
            request.setAttribute("pendingProducts", pendingProducts);
            request.setAttribute("totalItemsSold", totalItemsSold);
            request.setAttribute("totalEarnings", totalEarnings);
            request.setAttribute("recentProducts", products.size() > 5 ? products.subList(0, 5) : products);

            request.getRequestDispatcher("/WEB-INF/jsp/seller/dashboard.jsp").forward(request, response);

        } else if ("/seller/products".equals(path)) {
            List<Product> products = productService.getProductsBySeller(seller.getId());
            request.setAttribute("products", products);
            request.getRequestDispatcher("/WEB-INF/jsp/seller/products.jsp").forward(request, response);

        } else if ("/seller/products/add".equals(path)) {
            request.setAttribute("formAction", "add");
            request.getRequestDispatcher("/WEB-INF/jsp/seller/product-form.jsp").forward(request, response);

        } else if ("/seller/products/edit".equals(path)) {
            String idParam = request.getParameter("id");
            if (idParam == null) {
                response.sendRedirect(request.getContextPath() + "/seller/products");
                return;
            }
            Long productId = Long.parseLong(idParam);
            Product product = productService.getProductById(productId);

            if (product == null || product.getSellerId() == null || !product.getSellerId().equals(seller.getId())) {
                request.getSession().setAttribute("errorMessage", "Access Denied: You can only edit your own products.");
                response.sendRedirect(request.getContextPath() + "/seller/products");
                return;
            }

            request.setAttribute("product", product);
            request.setAttribute("formAction", "edit");
            request.getRequestDispatcher("/WEB-INF/jsp/seller/product-form.jsp").forward(request, response);

        } else if ("/seller/orders".equals(path)) {
            List<OrderItem> salesItems = orderDAO.findOrderItemsBySeller(seller.getId());
            request.setAttribute("salesItems", salesItems);
            request.getRequestDispatcher("/WEB-INF/jsp/seller/orders.jsp").forward(request, response);

        } else {
            response.sendRedirect(request.getContextPath() + "/seller/dashboard");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User seller = getLoggedInUser(request);
        String path = request.getServletPath();
        HttpSession session = request.getSession();

        try {
            if ("/seller/products/add".equals(path)) {
                String name = request.getParameter("name");
                String description = request.getParameter("description");
                BigDecimal price = new BigDecimal(request.getParameter("price"));
                String category = request.getParameter("category");
                int stock = Integer.parseInt(request.getParameter("stock"));
                String imageUrl = request.getParameter("imageUrl");

                Product product = new Product(name, description, price, category, stock, imageUrl);
                product.setSellerId(seller.getId());
                product.setStatus("APPROVED"); // Default approved or pending

                productService.addProduct(product);
                session.setAttribute("successMessage", "Product listed successfully!");
                response.sendRedirect(request.getContextPath() + "/seller/products");
                return;

            } else if ("/seller/products/edit".equals(path)) {
                Long productId = Long.parseLong(request.getParameter("id"));
                Product existing = productService.getProductById(productId);

                if (existing == null || existing.getSellerId() == null || !existing.getSellerId().equals(seller.getId())) {
                    session.setAttribute("errorMessage", "Access Denied: You can only edit your own products.");
                    response.sendRedirect(request.getContextPath() + "/seller/products");
                    return;
                }

                existing.setName(request.getParameter("name"));
                existing.setDescription(request.getParameter("description"));
                existing.setPrice(new BigDecimal(request.getParameter("price")));
                existing.setCategory(request.getParameter("category"));
                existing.setStock(Integer.parseInt(request.getParameter("stock")));
                existing.setImageUrl(request.getParameter("imageUrl"));

                productService.updateProduct(existing);
                session.setAttribute("successMessage", "Product updated successfully!");
                response.sendRedirect(request.getContextPath() + "/seller/products");
                return;

            } else if ("/seller/products/delete".equals(path)) {
                Long productId = Long.parseLong(request.getParameter("id"));
                boolean deleted = productService.deleteProduct(productId, seller.getId());
                if (deleted) {
                    session.setAttribute("successMessage", "Product deleted successfully.");
                } else {
                    session.setAttribute("errorMessage", "Could not delete product or product does not belong to you.");
                }
                response.sendRedirect(request.getContextPath() + "/seller/products");
                return;
            }
        } catch (IllegalArgumentException e) {
            session.setAttribute("errorMessage", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/seller/products");
            return;
        } catch (Exception e) {
            session.setAttribute("errorMessage", "Failed to save product: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/seller/products");
            return;
        }

        response.sendRedirect(request.getContextPath() + "/seller/dashboard");
    }

    private User getLoggedInUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (session != null) ? (User) session.getAttribute("user") : null;
    }
}
