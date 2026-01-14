package com.laptrinhweb.zerostarcafe.web.common.filters;

import com.laptrinhweb.zerostarcafe.core.context.LocaleContext;
import com.laptrinhweb.zerostarcafe.core.utils.LoggerUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * <h2>Description:</h2>
 * <p>
 * Central filter for cleaning up ThreadLocal resources and other request-scoped resources.
 * Ensures proper cleanup even when exceptions occur during request processing.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 05/01/2026
 * @since 1.0.0
 */
@WebFilter(filterName = "ResourceCleanupFilter", urlPatterns = {"/*"})
public class ResourceCleanupFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LoggerUtil.info(ResourceCleanupFilter.class,
                "ResourceCleanupFilter initialized.");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp,
                         FilterChain chain) throws IOException, ServletException {
        try {
            // Continue with request processing
            chain.doFilter(req, resp);

        } finally {
            HttpServletRequest request = (HttpServletRequest) req;
            HttpServletResponse response = (HttpServletResponse) resp;

            // Cleanup resources
            cleanupResources(request, response);
        }
    }

    /**
     * Centralized cleanup method for all request-scoped resources.
     */
    private void cleanupResources(HttpServletRequest request, HttpServletResponse response) {
        String uri = request.getRequestURI();

        // 1. Clear LocaleContext
        try {
            if (LocaleContext.isSet()) {
                LocaleContext.clear();
                LoggerUtil.debug(ResourceCleanupFilter.class,
                        "Cleared LocaleContext for request: " + uri);
            }
        } catch (Exception e) {
            LoggerUtil.error(ResourceCleanupFilter.class,
                    "Failed to clear LocaleContext for request: " + uri, e);
        }

    }

    @Override
    public void destroy() {
        LoggerUtil.info(ResourceCleanupFilter.class,
                "ResourceCleanupFilter destroyed");
    }
}
