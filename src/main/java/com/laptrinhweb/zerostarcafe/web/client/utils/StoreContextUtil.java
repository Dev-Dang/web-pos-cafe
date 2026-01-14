package com.laptrinhweb.zerostarcafe.web.client.utils;

import com.laptrinhweb.zerostarcafe.core.security.AppCookie;
import com.laptrinhweb.zerostarcafe.core.security.CookieUtils;
import com.laptrinhweb.zerostarcafe.core.utils.TimeUtil;
import com.laptrinhweb.zerostarcafe.domain.store.model.StoreConstants;
import com.laptrinhweb.zerostarcafe.domain.store.model.StoreContext;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Helper for saving store context to session and cookies.
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 12/12/2025
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreContextUtil {

    /**
     * Save store context into the session.
     *
     * @param req the incoming HTTP request
     * @param ctx the store context to save
     */
    public static void bind(HttpServletRequest req, StoreContext ctx) {
        if (ctx == null)
            return;

        HttpSession session = req.getSession();
        session.setAttribute(StoreConstants.Attribute.CURRENT_STORE_CTX, ctx);
    }

    /**
     * Save store context to session and cookies.
     *
     * @param req  the incoming HTTP request
     * @param resp the HTTP response used to add cookies
     * @param ctx  the store context to store
     */
    public static void persist(
            HttpServletRequest req,
            HttpServletResponse resp,
            StoreContext ctx
    ) {
        if (ctx == null)
            return;

        // Save store context to the current session
        bind(req, ctx);

        // Compute cookie TTL
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expired = now.plusMinutes(StoreConstants.Cookie.MAX_AGE_SECONDS);
        int ttlSeconds = TimeUtil.ttlFromNow(expired);

        // Save storeId and tableId to cookies
        if (ctx.getStoreId() != null) {
            Cookie storeIdCookie = AppCookie.accessible(
                    StoreConstants.Cookie.LAST_STORE_ID,
                    String.valueOf(ctx.getStoreId()),
                    ttlSeconds
            );
            resp.addCookie(storeIdCookie);
        }

        if (ctx.getTableId() != null) {
            Cookie tableIdCookie = AppCookie.accessible(
                    StoreConstants.Cookie.LAST_TABLE_ID,
                    String.valueOf(ctx.getTableId()),
                    ttlSeconds
            );
            resp.addCookie(tableIdCookie);
        } else {
            // Remove stale table selection when tableId is not provided.
            CookieUtils.clear(StoreConstants.Cookie.LAST_TABLE_ID, resp);
        }
    }
}