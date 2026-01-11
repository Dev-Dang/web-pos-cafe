package com.laptrinhweb.zerostarcafe.web.auth.session;

import com.laptrinhweb.zerostarcafe.core.security.SecurityKeys;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSession;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Initializes the shared AuthSessionManager at application startup.
 *
 * @author Dang Van Trung
 * @version 1.1.0
 * @lastModified 22/11/2025
 * @since 1.0.0
 */
@WebListener
public class AuthSessionListener implements ServletContextListener {

    private AuthSessionManager sessionManager;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext ctx = event.getServletContext();

        // Enforce single active session per user
        var userSessions = new ConcurrentHashMap<Long, HttpSession>();
        sessionManager = new AuthSessionManager(userSessions);

        ctx.setAttribute(SecurityKeys.CTX_AUTH_SESSION_MANAGER, sessionManager);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        ServletContext ctx = event.getServletContext();
        sessionManager.getUserSessions().clear();
        ctx.removeAttribute(SecurityKeys.CTX_AUTH_SESSION_MANAGER);
    }
}