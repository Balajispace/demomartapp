package com.balajimart.controller;

import com.balajimart.model.User;
import com.balajimart.service.AuthService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

    private AuthService authService;

    @Override
    public void init() throws ServletException {
        this.authService = new AuthService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            if (user.isAdmin()) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else if (user.isSeller()) {
                response.sendRedirect(request.getContextPath() + "/seller/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/home");
            }
            return;
        }

        String role = request.getParameter("role");
        if (role != null) {
            request.setAttribute("selectedRole", role.toUpperCase());
        }

        request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String role = request.getParameter("role");

        try {
            User newUser = authService.registerUser(name, email, password, confirmPassword, role);
            HttpSession session = request.getSession(true);
            session.setAttribute("user", newUser);
            session.setAttribute("successMessage", "Account created successfully! Welcome to BALAJIMART.");

            if (newUser.isSeller()) {
                response.sendRedirect(request.getContextPath() + "/seller/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/home");
            }
        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.setAttribute("name", name);
            request.setAttribute("email", email);
            request.setAttribute("selectedRole", role != null ? role.toUpperCase() : "BUYER");
            request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
        }
    }
}
