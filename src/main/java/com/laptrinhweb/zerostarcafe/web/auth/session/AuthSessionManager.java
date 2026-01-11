package com.laptrinhweb.zerostarcafe.web.auth.session;

import com.laptrinhweb.zerostarcafe.core.security.AppCookie;
import com.laptrinhweb.zerostarcafe.core.security.CookieUtil;
import com.laptrinhweb.zerostarcafe.core.security.SecurityKeys;
import com.laptrinhweb.zerostarcafe.core.utils.TimeUtil;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthContext;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthToken;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthUser;
import com.laptrinhweb.zerostarcafe.web.auth.mapper.AuthWebMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <h2>Description:</h2>
 * <p>
 * Manages user HTTP sessions at application level. It keeps track of
 * the active session per user, updates session data, writes cookies,
 * and removes old sessions when needed.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>{@code
 * AuthContext ctx = sessionManager.getContext(request);
 * sessionManager.startSession(request, response, ctx);
 * }</pre>
 *
 * @author Dang Van Trung
 * @version 1.1.1
 * @lastModified 13/12/2025
 * @since 1.0.0
 */

@Getter
public final class AuthSessionManager {

    private final ConcurrentHashMap<Long, HttpSession> userSessions;

    public AuthSessionManager(ConcurrentHashMap<Long, HttpSession> userSessions) {
        this.userSessions = userSessions;
    }

    /**
     * Builds an AuthContext from the current HttpSession.
     * If the session does not exist, null is returned.
     *
     * @param request incoming HTTP request
     * @return AuthContext or null if no session is found
     */
    public AuthContext getContext(HttpServletRequest request) {
        if (request == null)
            return null;

        HttpSession session = request.getSession(false);
        if (session == null)
            return null;

        return AuthWebMapper.toAuthContext(session);
    }

    /**
     * Creates a new session for the authenticated user.
     * The session ID is rotated, the user is stored in the session,
     * and all auth cookies are written to the response.
     *
     * @param request  current HTTP request
     * @param response current HTTP response
     * @param context  authenticated context to store
     */
    public void startSession(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthContext context
    ) {

        if (context == null || !context.isValid())
            return;

        HttpSession session = request.getSession();
        request.changeSessionId();

        AuthUser user = context.getAuthUser();
        if (user != null) {
            revokeSession(user.getId());
            userSessions.put(user.getId(), session);
        }

        applyContext(session, context);
        writeCookies(response, context);
    }

    /**
     * Updates an existing session after a successful re-authentication
     * or token rotation. If no session exists, a new one will be created.
     *
     * @param request  current HTTP request
     * @param response current HTTP response
     * @param context  new authenticated context to apply
     */
    public void refreshSession(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthContext context
    ) {

        if (context == null || !context.isValid()) {
            return;
        }

        HttpSession session = request.getSession(false);
        if (session == null) {
            startSession(request, response, context);
            return;
        }

        request.changeSessionId();
        applyContext(session, context);
        writeCookies(response, context);
    }

    /**
     * Ends the active session and removes all authentication cookies.
     * If the user has an active session in memory, it will also be revoked.
     *
     * @param request  current HTTP request
     * @param response current HTTP response
     */
    public void endSession(HttpServletRequest request,
                           HttpServletResponse response
    ) {

        HttpSession session = request.getSession(false);

        if (session == null) {
            clearAuthCookies(request, response);
            return;
        }

        AuthContext context = AuthWebMapper.toAuthContext(session);
        if (context != null && context.isValid()) {
            AuthUser user = context.getAuthUser();
            if (user != null) {
                revokeSession(user.getId());
            }
        }

        try {
            session.invalidate();
        } catch (IllegalStateException ignore) {
            // Already invalidated
        }

        clearAuthCookies(request, response);
    }

    /**
     * Removes the active session of a specific user.
     * If a session exists, it will be invalidated and deleted.
     *
     * @param userId ID of the user whose session should be revoked
     */
    public void revokeSession(Long userId) {
        if (userId == null)
            return;

        HttpSession currentSession = userSessions.remove(userId);
        if (currentSession != null) {
            try {
                currentSession.invalidate();
            } catch (IllegalStateException ignore) {
                // Already invalidated
            }
        }
    }

    /**
     * Stores AuthContext data inside the session.
     */
    private void applyContext(HttpSession session, AuthContext context) {
        session.setAttribute(SecurityKeys.SESSION_AUTH_USER, context.getAuthUser());
        session.setAttribute(SecurityKeys.SESSION_AUTH_SESSION, context.getSessionInfo());
        session.setAttribute(SecurityKeys.SESSION_AUTH_TOKENS, context.getTokens());
    }

    /**
     * Writes all authentication cookies to the response.
     */
    private void writeCookies(HttpServletResponse response, AuthContext context) {
        List<AuthToken> tokens = context.getTokens();
        if (tokens.isEmpty()) {
            return;
        }

        for (AuthToken token : tokens) {
            Cookie cookie = AppCookie.strict(
                    token.getName(),
                    token.getValue(),
                    TimeUtil.ttlFromNow(token.getExpiredAt())
            );
            response.addCookie(cookie);
        }
    }

    /**
     * Removes all authentication cookies from the client.
     */
    private void clearAuthCookies(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        Map<String, String> cookies = CookieUtil.getAll(request);
        if (cookies.isEmpty())
            return;

        String prefix = SecurityKeys.AUTH_COOKIE_PREFIX;

        for (String name : cookies.keySet()) {
            if (name != null && name.startsWith(prefix)) {
                CookieUtil.clear(name, response);
            }
        }
    }
}