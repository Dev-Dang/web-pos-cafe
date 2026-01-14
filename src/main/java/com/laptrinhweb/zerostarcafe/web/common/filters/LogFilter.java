package com.laptrinhweb.zerostarcafe.web.common.filters;

import com.laptrinhweb.zerostarcafe.core.security.SecurityKeys;
import com.laptrinhweb.zerostarcafe.core.utils.LoggerUtil;
import com.laptrinhweb.zerostarcafe.web.common.utils.RequestUtils;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Logs each dynamic HTTP request with method, URI, status, and response time.
 * Also logs CSRF token information for debugging state-changing requests.
 *
 * @author Dang Van Trung
 * @version 1.0.5
 * @lastModified 03/01/2026
 * @since 1.0.0
 */
@WebFilter(filterName = "LogFilter", urlPatterns = "/*")
public class LogFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        String uri = request.getRequestURI();
        long start = System.currentTimeMillis();

        // Skip static files
        if (RequestUtils.isStaticRequest(request)) {
            chain.doFilter(req, resp);
            return;
        }

        // Log CSRF token info for state-changing methods
        if (!"GET".equals(request.getMethod())) {
            logCsrfDebugInfo(request);
        }

        chain.doFilter(req, resp);

        long duration = System.currentTimeMillis() - start;

        LoggerUtil.info(
                LogFilter.class,
                String.format("%-4s %-40s | %3d | %4d ms",
                        request.getMethod(),
                        uri,
                        response.getStatus(),
                        duration)
        );
    }

    /**
     * Logs CSRF token information for debugging state-changing requests.
     */
    private void logCsrfDebugInfo(HttpServletRequest request) {
        String headerToken = request.getHeader(SecurityKeys.CSRF_HEADER_NAME);
        String paramToken = request.getParameter(SecurityKeys.CSRF_PARAM_NAME);
        
        LoggerUtil.debug(getClass(),
            String.format("[CSRF Debug] URI: %s | Header: %s | Param: %s",
                request.getRequestURI(),
                headerToken != null ? "✓" : "✗",
                paramToken != null ? "✓" : "✗"));
    }
}