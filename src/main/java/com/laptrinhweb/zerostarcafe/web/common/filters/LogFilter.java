package com.laptrinhweb.zerostarcafe.web.common.filters;

import com.laptrinhweb.zerostarcafe.core.utils.LoggerUtil;
import com.laptrinhweb.zerostarcafe.core.utils.PathUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Logs each dynamic HTTP request with method, URI, status, and response time.
 *
 * @author Dang Van Trung
 * @version 1.0.2
 * @lastModified 23/11/2025
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

        // Skip static file
        String path = uri.substring(request.getContextPath().length());

        if (PathUtil.isStatic(path)) {
            chain.doFilter(req, resp);
            return;
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
}