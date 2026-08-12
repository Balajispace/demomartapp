package com.balajimart.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@WebListener
public class DatabaseInitializer implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("Initializing BALAJIMART database schema and seed data...");
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement()) {

            // Create USERS table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(100) NOT NULL,
                    email VARCHAR(100) NOT NULL UNIQUE,
                    password_hash VARCHAR(255) NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """);

            // Create PRODUCTS table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS products (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(150) NOT NULL,
                    description TEXT,
                    price DECIMAL(10,2) NOT NULL,
                    category VARCHAR(50) NOT NULL,
                    stock INT NOT NULL DEFAULT 0,
                    image_url VARCHAR(255),
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """);

            // Create CART_ITEMS table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS cart_items (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    user_id BIGINT NOT NULL,
                    product_id BIGINT NOT NULL,
                    quantity INT NOT NULL DEFAULT 1,
                    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
                )
            """);

            // Create ORDERS table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS orders (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    user_id BIGINT NOT NULL,
                    total_amount DECIMAL(10,2) NOT NULL,
                    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
                )
            """);

            // Create ORDER_ITEMS table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS order_items (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    order_id BIGINT NOT NULL,
                    product_id BIGINT NOT NULL,
                    quantity INT NOT NULL,
                    unit_price DECIMAL(10,2) NOT NULL,
                    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
                    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
                )
            """);

            logger.info("Database tables verified/created successfully.");

            // Check if PRODUCTS table is empty, then seed
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM products");
            if (rs.next() && rs.getInt(1) == 0) {
                seedProducts(stmt);
                logger.info("Sample products seeded successfully.");
            }

        } catch (Exception e) {
            logger.error("Error initializing database schema", e);
            throw new RuntimeException("Database initialization failed", e);
        }
    }

    private void seedProducts(Statement stmt) throws Exception {
        String[] seedQueries = new String[]{
            "INSERT INTO products (name, description, price, category, stock, image_url) VALUES (" +
                "'Wireless Bluetooth Headphones', 'Premium over-ear noise isolating wireless headphones with deep bass and 30-hour battery life.', 2499.00, 'Electronics', 25, 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&w=600&q=80')",

            "INSERT INTO products (name, description, price, category, stock, image_url) VALUES (" +
                "'USB-C Fast Charger', '65W GaN Dual Port USB-C fast wall charger suitable for laptops, tablets, and smartphones.', 1199.00, 'Electronics', 50, 'https://images.unsplash.com/photo-1583863788434-e58a36330cf0?auto=format&fit=crop&w=600&q=80')",

            "INSERT INTO products (name, description, price, category, stock, image_url) VALUES (" +
                "'Mechanical Keyboard', 'RGB backlit mechanical gaming keyboard with tactile blue switches and ergonomic wrist rest.', 3499.00, 'Electronics', 15, 'https://images.unsplash.com/photo-1587829741301-dc798b83add3?auto=format&fit=crop&w=600&q=80')",

            "INSERT INTO products (name, description, price, category, stock, image_url) VALUES (" +
                "'Wireless Mouse', 'Ergonomic 2.4GHz optical wireless mouse with silent clicks and adjustable DPI levels.', 799.00, 'Electronics', 40, 'https://images.unsplash.com/photo-1615663245857-ac93bb7c39e7?auto=format&fit=crop&w=600&q=80')",

            "INSERT INTO products (name, description, price, category, stock, image_url) VALUES (" +
                "'Java Programming Guide', 'Comprehensive master guide covering Java 17, Object-Oriented Design, Servlets, and JDBC fundamentals.', 699.00, 'Books', 30, 'https://images.unsplash.com/photo-1532012197267-da84d127e765?auto=format&fit=crop&w=600&q=80')",

            "INSERT INTO products (name, description, price, category, stock, image_url) VALUES (" +
                "'Data Structures and Algorithms Book', 'In-depth algorithms textbook with step-by-step illustrations, complexity analysis, and problem sets.', 850.00, 'Books', 20, 'https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?auto=format&fit=crop&w=600&q=80')",

            "INSERT INTO products (name, description, price, category, stock, image_url) VALUES (" +
                "'Cotton Casual Shirt', '100% breathable combed cotton slim-fit casual shirt suitable for work and everyday wear.', 1299.00, 'Clothing', 35, 'https://images.unsplash.com/photo-1596755094514-f87e34085b2c?auto=format&fit=crop&w=600&q=80')",

            "INSERT INTO products (name, description, price, category, stock, image_url) VALUES (" +
                "'Laptop Backpack', 'Water-resistant multi-compartment travel laptop backpack fitting up to 15.6 inch laptops.', 1899.00, 'Accessories', 28, 'https://images.unsplash.com/photo-1553062407-98eeb64c6a62?auto=format&fit=crop&w=600&q=80')",

            "INSERT INTO products (name, description, price, category, stock, image_url) VALUES (" +
                "'Stainless Steel Water Bottle', 'Double-wall vacuum insulated 1-liter thermal flask keeping drinks cold for 24 hours.', 649.00, 'Home', 60, 'https://images.unsplash.com/photo-1602143407151-7111542de6e8?auto=format&fit=crop&w=600&q=80')",

            "INSERT INTO products (name, description, price, category, stock, image_url) VALUES (" +
                "'LED Desk Lamp', 'Dimmable touch-controlled modern LED desk lamp with 5 color modes and built-in USB charging port.', 999.00, 'Home', 22, 'https://images.unsplash.com/photo-1534073828943-f801091bb18c?auto=format&fit=crop&w=600&q=80')"
        };

        for (String sql : seedQueries) {
            stmt.execute(sql);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("Closing BALAJIMART database connections...");
        DatabaseConfig.shutdown();
    }
}
