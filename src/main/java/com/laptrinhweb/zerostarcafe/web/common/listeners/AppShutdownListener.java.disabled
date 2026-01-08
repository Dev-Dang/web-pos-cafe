package com.laptrinhweb.zerostarcafe.web.common.listeners;

import com.laptrinhweb.zerostarcafe.core.utils.LoggerUtil;
import com.laptrinhweb.zerostarcafe.domain.cart.service.CartCacheService;
import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

/**
 * <h2>Description:</h2>
 * <p>
 * Listener for application startup and shutdown events.
 * Handles resource cleanup when the application stops.
 * </p>
 *
 * <h2>Responsibilities:</h2>
 * <ul>
 *     <li>Flush cart cache to database on shutdown</li>
 *     <li>Clean up MySQL connection cleanup thread</li>
 *     <li>Unregister JDBC drivers to prevent memory leaks</li>
 * </ul>
 *
 * @author Dang Van Trung
 * @version 1.1.0
 * @lastModified 29/12/2025
 * @since 1.0.0
 */
@WebListener
public class AppShutdownListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LoggerUtil.info(AppShutdownListener.class,
                "🚀 Zero Star Cafe application is ready !");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LoggerUtil.info(AppShutdownListener.class,
                "🔄 Zero Star Cafe application is shutting down...");

        // Flush all dirty carts to DB first
        flushCartCache();

        // Clean up MySQL connection cleanup thread
        cleanupMySQLConnectionCleanupThread();

        // Unregister all JDBC drivers to prevent memory leaks
        unregisterJDBCDrivers();

        LoggerUtil.info(AppShutdownListener.class,
                "✅ Zero Star Cafe application shutdown completed successfully.");
    }

    /**
     * Flushes all dirty carts from cache to database before shutdown.
     */
    private void flushCartCache() {
        try {
            LoggerUtil.info(AppShutdownListener.class,
                    "🛒 Shutting down cart cache service...");

            CartCacheService.getInstance().shutdown();

            LoggerUtil.info(AppShutdownListener.class,
                    "✅ Cart cache shutdown completed.");
        } catch (Exception e) {
            LoggerUtil.error(AppShutdownListener.class,
                    "❌ Error while shutting down cart cache: " + e.getMessage(), e);
        }
    }

    /**
     * Shuts down the MySQL abandoned connection cleanup thread to prevent memory leaks.
     */
    private void cleanupMySQLConnectionCleanupThread() {
        try {
            LoggerUtil.info(AppShutdownListener.class,
                    "🧹 Shutting down MySQL abandoned connection cleanup thread...");

            AbandonedConnectionCleanupThread.checkedShutdown();

            LoggerUtil.info(AppShutdownListener.class,
                    "✅ MySQL cleanup thread shutdown completed.");
        } catch (Exception e) {
            LoggerUtil.warn(AppShutdownListener.class,
                    "⚠️ Error while shutting down MySQL cleanup thread: " + e.getMessage());
        }
    }

    /**
     * Unregisters all JDBC drivers that were registered by this web application.
     */
    private void unregisterJDBCDrivers() {
        ClassLoader webappClassLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<Driver> drivers = DriverManager.getDrivers();

        LoggerUtil.info(AppShutdownListener.class,
                "🧹 Unregistering JDBC drivers...");

        int unregisteredCount = 0;

        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();

            // Only unregister drivers loaded by this web application's class loader
            if (driver.getClass().getClassLoader() == webappClassLoader) {
                try {
                    LoggerUtil.info(AppShutdownListener.class,
                            "Unregistering JDBC driver: " + driver.getClass().getName());

                    DriverManager.deregisterDriver(driver);
                    unregisteredCount++;

                } catch (SQLException e) {
                    LoggerUtil.error(AppShutdownListener.class,
                            "❌ Failed to unregister JDBC driver: " + driver.getClass().getName(), e);
                }
            }
        }

        if (unregisteredCount > 0) {
            LoggerUtil.info(AppShutdownListener.class,
                    "✅ Successfully unregistered " + unregisteredCount + " JDBC driver(s).");
        } else {
            LoggerUtil.info(AppShutdownListener.class,
                    "ℹ️ No JDBC drivers to unregister for this web application.");
        }
    }
}
