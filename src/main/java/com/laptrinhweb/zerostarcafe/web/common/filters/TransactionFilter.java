package com.laptrinhweb.zerostarcafe.web.common.filters;

import com.laptrinhweb.zerostarcafe.core.context.DBContext;
import com.laptrinhweb.zerostarcafe.core.utils.LoggerUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * <h2>Description:</h2>
 * <p>
 * Servlet filter that finalizes the transaction for the current HTTP request.
 * If application code acquires a connection via {@link DBContext#getOrCreate()},
 * this filter commits after successful processing, rolls back on errors,
 * closes the connection, and clears the thread-local context.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 05/01/2026
 * @since 1.0.0
 */
@WebFilter(filterName = "TransactionFilter", urlPatterns = "/*")
public class TransactionFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        String requestUri = request.getRequestURI();

        try {
            // Proceed with the next filter or servlet
            chain.doFilter(req, resp);

            // Commit transaction if everything is fine
            Connection conn = DBContext.get();
            if (conn != null) {
                conn.commit();
            }

        } catch (Exception e) {
            // Rollback transaction if any exception occurs
            Connection conn = DBContext.get();
            safeRollback(conn, requestUri);

            // Re-throw the exception to be handled by the container
            throw new ServletException(e);

        } finally {
            // Clean up the connection context
            Connection conn = DBContext.get();
            safeClose(conn, requestUri);

            // Clear the DBContext for the current thread
            DBContext.clear();
        }
    }

    /**
     * Roll back the transaction, logging any rollback failures.
     */
    private static void safeRollback(Connection conn, String requestUri) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                LoggerUtil.error(TransactionFilter.class,
                        "Failed to rollback for request: " + requestUri, ex);
            }
        }
    }

    /**
     * Close the connection, logging any close failures.
     */
    private static void safeClose(Connection conn, String requestUri) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                LoggerUtil.error(TransactionFilter.class,
                        "Failed to close connection for request: " + requestUri, ex);
            }
        }
    }
}
