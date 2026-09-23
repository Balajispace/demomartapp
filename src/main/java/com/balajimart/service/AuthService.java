package com.balajimart.service;

import com.balajimart.dao.UserDAO;
import com.balajimart.model.User;
import com.balajimart.util.PasswordUtil;
import com.balajimart.util.ValidationUtil;

public class AuthService {

    private final UserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    public AuthService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User registerUser(String name, String email, String password, String confirmPassword) throws IllegalArgumentException {
        return registerUser(name, email, password, confirmPassword, "BUYER");
    }

    public User registerUser(String name, String email, String password, String confirmPassword, String role) throws IllegalArgumentException {
        if (!ValidationUtil.isNotEmpty(name)) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (!ValidationUtil.isNotEmpty(email)) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (!ValidationUtil.isValidEmail(email)) {
            throw new IllegalArgumentException("Please enter a valid email address");
        }
        if (!ValidationUtil.isNotEmpty(password)) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Password and confirm password must match");
        }

        String userRole = (role != null && !role.trim().isEmpty()) ? role.trim().toUpperCase() : "BUYER";
        if ("ADMIN".equals(userRole)) {
            throw new IllegalArgumentException("Admin registration is not allowed. Please contact system administrator.");
        }
        if (!"BUYER".equals(userRole) && !"SELLER".equals(userRole)) {
            throw new IllegalArgumentException("Invalid account type selected.");
        }

        User existingUser = userDAO.findByEmail(email);
        if (existingUser != null) {
            throw new IllegalArgumentException("Email is already registered");
        }

        String hashedPassword = PasswordUtil.hashPassword(password);
        User newUser = new User(name.trim(), email.trim().toLowerCase(), hashedPassword, userRole);
        return userDAO.createUser(newUser);
    }

    public User loginUser(String email, String password) throws IllegalArgumentException {
        return loginUser(email, password, null);
    }

    public User loginUser(String email, String password, String expectedRole) throws IllegalArgumentException {
        if (!ValidationUtil.isNotEmpty(email) || !ValidationUtil.isNotEmpty(password)) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        User user = userDAO.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        boolean passwordValid = PasswordUtil.checkPassword(password, user.getPasswordHash());
        if (!passwordValid) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        if (!user.isActive()) {
            throw new IllegalArgumentException("Your account has been deactivated. Please contact support.");
        }

        if (expectedRole != null && !expectedRole.trim().isEmpty()) {
            if (!expectedRole.trim().equalsIgnoreCase(user.getRole())) {
                throw new IllegalArgumentException("Selected role does not match account role (" + user.getRole() + ")");
            }
        }

        return user;
    }
}
