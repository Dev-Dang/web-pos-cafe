package com.laptrinhweb.zerostarcafe.core.context;

import com.laptrinhweb.zerostarcafe.core.database.DBConnection;
import com.laptrinhweb.zerostarcafe.core.transaction.TransactionManager;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * <h2>Description:</h2>
 * <p>
 * Thread-local holder for a JDBC {@link Connection} bound to the current
 * request or unit of work. The connection is created lazily from
 * {@link DBConnection} when {@link #getOrCreate()} is called and is configured
 * with auto-commit disabled so a surrounding filter can commit or roll back.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * Connection conn = DBContext.getOrCreate();
 * try {
 *      // do database operations with conn
 * } catch (SQLException e) {
 *      // If any exception occurs, wrap and rethrow as AppException
 *      throw new AppException("Database error", e);
 * }
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 05/01/2026
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DBContext {

    private static final ThreadLocal<Connection> connHolder = new ThreadLocal<>();

    /**
     * Get connection of the current context.
     *
     * @return Connection or null if not set
     */
    public static Connection get() {
        return connHolder.get();
    }

    /**
     * Get connection of the current request.
     * If not set, get one from pool and set it to the context.
     * The returned connection has auto-commit disabled.
     * 
     * Priority:
     * 1. Active explicit transaction (from TransactionManager)
     * 2. Filter-managed connection (existing thread-local)
     * 3. Create new connection for filter
     *
     * @return Connection for the current request
     * @throws SQLException if a database access error occurs
     */
    public static Connection getOrCreate() throws SQLException {
        // Priority 1: Active explicit transaction
        Connection txConn = TransactionManager.getCurrentConnection();
        if (txConn != null) return txConn;

        // Priority 2: Filter-managed connection
        Connection conn = connHolder.get();
        if (conn != null) return conn;

        // Priority 3: Create new connection for filter
        conn = DBConnection.getConnection();
        conn.setAutoCommit(false);
        connHolder.set(conn);
        return conn;
    }

    /**
     * Clears the current thread's database connection.
     */
    public static void clear() {
        connHolder.remove();
    }
}
