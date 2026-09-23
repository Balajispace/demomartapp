package com.balajimart.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConfig {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);
    private static HikariDataSource dataSource;

    static {
        try {
            Properties props = new Properties();
            InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream("db.properties");
            if (input != null) {
                props.load(input);
            } else {
                logger.warn("db.properties not found, using default H2 configuration");
            }

            HikariConfig config = new HikariConfig();
            config.setDriverClassName(props.getProperty("db.driver", "org.h2.Driver"));
            config.setJdbcUrl(props.getProperty("db.url", "jdbc:h2:file:./data/balajimartdb;DB_CLOSE_DELAY=-1;MODE=LEGACY;AUTO_SERVER=TRUE"));
            config.setUsername(props.getProperty("db.user", "sa"));
            config.setPassword(props.getProperty("db.password", ""));
            
            config.setMaximumPoolSize(Integer.parseInt(props.getProperty("hikari.maximumPoolSize", "10")));
            config.setMinimumIdle(Integer.parseInt(props.getProperty("hikari.minimumIdle", "2")));
            config.setIdleTimeout(Long.parseLong(props.getProperty("hikari.idleTimeout", "30000")));
            config.setConnectionTimeout(Long.parseLong(props.getProperty("hikari.connectionTimeout", "10000")));

            dataSource = new HikariDataSource(config);
            logger.info("HikariCP DataSource initialized successfully");
        } catch (Exception e) {
            logger.error("Failed to initialize HikariCP DataSource", e);
            throw new RuntimeException("Database initialization error", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("HikariDataSource is not initialized");
        }
        return dataSource.getConnection();
    }

    public static HikariDataSource getDataSource() {
        return dataSource;
    }

    public static void shutdown() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            logger.info("HikariCP DataSource closed successfully");
        }
    }
}
