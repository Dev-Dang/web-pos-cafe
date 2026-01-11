package com.laptrinhweb.zerostarcafe.web.client.utils;

import com.laptrinhweb.zerostarcafe.core.security.AppCookie;
import com.laptrinhweb.zerostarcafe.core.security.CookieUtil;
import com.laptrinhweb.zerostarcafe.core.utils.TimeUtil;
import com.laptrinhweb.zerostarcafe.domain.store.model.StoreConstants;
import com.laptrinhweb.zerostarcafe.domain.store.model.StoreContext;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.time.LocalDateTime;

/**
 * <h2>Description:</h2>
 * <p>
 * Utility class for storing and loading the current store context.
 * It helps save the context into session and also write related cookies
 * such as storeId and tableId for later requests.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * StoreContext ctx = ...;
 * StoreContextUtil.persist(req, resp, ctx);
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 12/12/2025
 * @since 1.0.0
 */
public class StoreContextUtil {

    private StoreContextUtil() {
    }

    /**
     * Saves the store context into the user's session.
     *
     * @param req the incoming HTTP request
     * @param ctx the store context to save
     */
    public static void bind(HttpServletRequest req, StoreContext ctx) {
        if (ctx == null)
            return;

        HttpSession session = req.getSession();
        session.setAttribute(StoreConstants.Session.CURRENT_STORE_CTX, ctx);
    }

    /**
     * Saves the store context to both session and cookies.
     * Writes storeId and tableId cookies if available.
     * Clears tableId cookie when the value is null.
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

        bind(req, ctx);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expired = now.plusMinutes(StoreConstants.Cookie.MAX_AGE_SECONDS);
        int ttlSeconds = TimeUtil.ttlFromNow(expired);

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
            CookieUtil.clear(StoreConstants.Cookie.LAST_TABLE_ID, resp);
        }
    }
}