package com.laptrinhweb.zerostarcafe.web.common.filters;

import com.laptrinhweb.zerostarcafe.core.exception.AppException;
import com.laptrinhweb.zerostarcafe.core.exception.BusinessException;
import com.laptrinhweb.zerostarcafe.core.exception.TransactionException;
import com.laptrinhweb.zerostarcafe.core.utils.LoggerUtil;
import com.laptrinhweb.zerostarcafe.web.common.routing.AppRoute;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Catches unhandled exceptions from the filter chain and sends a 500 error
 * to the container's error handler.
 *
 * @author Dang Van Trung
 * @version 1.0.3
 * @lastModified 04/01/2026
 * @since 1.0.0
 */
@WebFilter(filterName = "ErrorFilter", urlPatterns = "/*")
public class ErrorFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        try {
            chain.doFilter(req, resp);

        } catch (BusinessException e) {
            // Business rule violation - 400 Bad Request
            if (response.isCommitted()) {
                LoggerUtil.warn(getClass(),
                        "Response already committed for BusinessException.");
                throw new ServletException(e);
            }

            LoggerUtil.info(getClass(),
                    "Business rule violation: " + e.getMessage());
            AppRoute.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage(), response);

        } catch (TransactionException | AppException e) {
            // Technical/Infrastructure errors - 500 Internal Server Error
            if (response.isCommitted()) {
                LoggerUtil.warn(getClass(),
                        "Response already committed for technical error.");
                throw new ServletException(e);
            }

            LoggerUtil.error(getClass(),
                    "Technical error: " + e.getClass().getSimpleName(), e);
            AppRoute.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Internal server error occurred", response);

        } catch (Exception e) {
            // Unknown errors - 500 Internal Server Error
            if (response.isCommitted()) {
                LoggerUtil.warn(getClass(),
                        "Response already committed for unknown error.");
                throw new ServletException(e);
            }

            LoggerUtil.error(getClass(), "Unhandled exception", e);
            AppRoute.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Internal server error occurred", response);
        }
    }
}