package com.balajimart.service;

import com.balajimart.dao.UserDAO;
import com.balajimart.model.User;
import com.balajimart.util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    private AuthService authService;
    private StubUserDAO stubUserDAO;

    // In-Memory Fake UserDAO for fast isolated unit testing
    private static class StubUserDAO extends UserDAO {
        private final Map<String, User> userByEmail = new HashMap<>();
        private long idCounter = 1;

        @Override
        public User findByEmail(String email) {
            return userByEmail.get(email.toLowerCase().trim());
        }

        @Override
        public User createUser(User user) {
            user.setId(idCounter++);
            userByEmail.put(user.getEmail().toLowerCase().trim(), user);
            return user;
        }
    }

    @BeforeEach
    void setUp() {
        stubUserDAO = new StubUserDAO();
        authService = new AuthService(stubUserDAO);
    }

    @Test
    @DisplayName("Should successfully register a new user with valid details")
    void testRegisterUserSuccess() {
        User registered = authService.registerUser("Alice Smith", "alice@example.com", "Secret123!", "Secret123!");

        assertNotNull(registered);
        assertEquals(1L, registered.getId());
        assertEquals("Alice Smith", registered.getName());
        assertEquals("alice@example.com", registered.getEmail());
        assertTrue(PasswordUtil.checkPassword("Secret123!", registered.getPasswordHash()));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when passwords do not match")
    void testRegisterUserPasswordMismatch() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                authService.registerUser("Bob", "bob@example.com", "Password123", "Mismatch123")
        );
        assertEquals("Password and confirm password must match", ex.getMessage());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when registering duplicate email")
    void testRegisterUserDuplicateEmail() {
        authService.registerUser("Alice", "alice@example.com", "Password123", "Password123");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                authService.registerUser("Alice Clone", "ALICE@EXAMPLE.COM", "Password123", "Password123")
        );
        assertEquals("Email is already registered", ex.getMessage());
    }

    @Test
    @DisplayName("Should successfully log in user with valid credentials")
    void testLoginUserSuccess() {
        authService.registerUser("Charlie", "charlie@example.com", "MyPassword123", "MyPassword123");

        User loggedIn = authService.loginUser("charlie@example.com", "MyPassword123");
        assertNotNull(loggedIn);
        assertEquals("Charlie", loggedIn.getName());
    }

    @Test
    @DisplayName("Should throw exception for incorrect password during login")
    void testLoginUserWrongPassword() {
        authService.registerUser("Charlie", "charlie@example.com", "MyPassword123", "MyPassword123");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                authService.loginUser("charlie@example.com", "WrongPassword")
        );
        assertEquals("Invalid email or password", ex.getMessage());
    }
}
