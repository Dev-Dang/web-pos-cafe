package com.laptrinhweb.zerostarcafe.web.common.filters;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

/**
 * Marks requests triggered by Unpoly so handlers can branch on fragment vs full-page flows.
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 01/01/2026
 * @since 1.0.0
 */
@WebFilter(filterName = "UnpolyFilter", urlPatterns = "/*")
public class UnpolyFilter implements Filter {

    public static final String HEADER_UP_VERSION = "X-Up-Version";
    public static final String ATTR_IS_UNPOLY = "isUnpoly";

    public static boolean isUnpoly(HttpServletRequest request) {
        if (request == null) return false;
        Object attr = request.getAttribute(ATTR_IS_UNPOLY);
        if (attr instanceof Boolean) return (Boolean) attr;
        return request.getHeader(HEADER_UP_VERSION) != null;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        boolean isUnpoly = request.getHeader(HEADER_UP_VERSION) != null;
        request.setAttribute(ATTR_IS_UNPOLY, isUnpoly);

        chain.doFilter(req, resp);
    }
}
