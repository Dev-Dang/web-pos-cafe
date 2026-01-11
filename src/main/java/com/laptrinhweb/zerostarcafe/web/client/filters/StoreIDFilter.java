package com.laptrinhweb.zerostarcafe.web.client.filters;

import com.laptrinhweb.zerostarcafe.core.security.CookieUtil;
import com.laptrinhweb.zerostarcafe.core.utils.PathUtil;
import com.laptrinhweb.zerostarcafe.domain.store.model.Store;
import com.laptrinhweb.zerostarcafe.domain.store.model.StoreConstants;
import com.laptrinhweb.zerostarcafe.domain.store.model.StoreContext;
import com.laptrinhweb.zerostarcafe.domain.store.service.StoreService;
import com.laptrinhweb.zerostarcafe.web.client.utils.StoreContextUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * <h2>Description:</h2>
 * <p>
 * A request filter that checks if the user has a valid StoreContext.
 * The filter loads the context from session, cookies, or fallback by IP.
 * If the context is found, it is saved into the session for later requests.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 13/12/2025
 * @since 1.0.0
 */
@WebFilter(filterName = "StoreIDFilter", urlPatterns = "/*")
public class StoreIDFilter implements Filter {

    private final StoreService storeService = new StoreService();

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        // 1. Skip static resources request
        String uri = request.getRequestURI();
        String path = uri.substring(request.getContextPath().length());
        if (PathUtil.isStatic(path)) {
            chain.doFilter(req, resp);
            return;
        }

        // 2. If store context already exists in session -> do nothing
        HttpSession session = request.getSession();
        if (session.getAttribute(StoreConstants.Session.CURRENT_STORE_CTX) != null) {
            chain.doFilter(req, resp);
            return;
        }

        // 3. Resolve context strategies (Cookie -> IP)
        StoreContext resolvedCtx = resolveFromCookie(request);
        if (resolvedCtx == null) {
            resolvedCtx = resolveFromGeoIp(request);
        }

        // 4. Update store context Session and Refresh Cookie if found
        if (resolvedCtx != null) {
            StoreContextUtil.bind(request, resolvedCtx);
        }

        chain.doFilter(req, resp);
    }

    /**
     * Tries to create a StoreContext by reading storeId and tableId
     * from browser cookies.
     *
     * @param request the current HTTP request
     * @return a valid StoreContext, or null if cookies are missing or invalid
     */
    private StoreContext resolveFromCookie(HttpServletRequest request) {
        String rawStoreId = CookieUtil.get(request, StoreConstants.Cookie.LAST_STORE_ID);
        String rawTableId = CookieUtil.get(request, StoreConstants.Cookie.LAST_TABLE_ID);

        if (rawStoreId == null || rawStoreId.isBlank())
            return null;

        try {
            long storeId = Long.parseLong(rawStoreId);

            // Validate if the store still exists/is active in DB
            Store store = storeService.getActiveStoreById(storeId);
            if (store == null)
                return null;

            // Table ID is optional, ignore errors if parsing fails
            Long tableId = null;
            if (rawTableId != null && !rawTableId.isBlank()) {
                try {
                    tableId = Long.parseLong(rawTableId);
                } catch (NumberFormatException ignored) {
                    // Invalid table ID, keep it null
                }
            }

            return new StoreContext(storeId, tableId);

        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    /**
     * Uses the client's IP address to find the nearest store.
     *
     * @param request the current HTTP request
     * @return a StoreContext based on IP lookup, or null if no store is found
     */
    private StoreContext resolveFromGeoIp(HttpServletRequest request) {
        String clientIp = request.getRemoteAddr();
        Store store = storeService.resolveStoreByReqIp(clientIp);

        if (store != null) {
            return new StoreContext(store.getId(), null);
        }

        return null;
    }
}