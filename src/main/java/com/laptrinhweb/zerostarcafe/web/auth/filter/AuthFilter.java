package com.laptrinhweb.zerostarcafe.web.auth.filter;

import com.laptrinhweb.zerostarcafe.core.security.SecurityKeys;
import com.laptrinhweb.zerostarcafe.core.utils.ContextUtil;
import com.laptrinhweb.zerostarcafe.core.utils.PathUtil;
import com.laptrinhweb.zerostarcafe.domain.auth.dto.RequestInfoDTO;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthContext;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthResult;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthStatus;
import com.laptrinhweb.zerostarcafe.domain.auth.service.AuthReqService;
import com.laptrinhweb.zerostarcafe.web.auth.mapper.AuthWebMapper;
import com.laptrinhweb.zerostarcafe.web.auth.session.AuthSessionManager;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * <h2>Description:</h2>
 * <p>
 * Global authentication filter. It checks user sessions,
 * validates auth cookies, restores missing sessions,
 * and supports token/session rotation.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.1.1
 * @lastModified 13/12/2025
 * @since 1.0.0
 */
@WebFilter(filterName = "AuthFilter", urlPatterns = "/*")
public class AuthFilter implements Filter {

    private AuthSessionManager sessionManager;
    private final AuthReqService authReqService = new AuthReqService();

    @Override
    public void init(FilterConfig config) throws ServletException {
        Filter.super.init(config);
        ServletContext ctx = config.getServletContext();

        // Load shared AuthSessionManager from servlet context
        this.sessionManager = ContextUtil.require(
                ctx, SecurityKeys.CTX_AUTH_SESSION_MANAGER, AuthSessionManager.class);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        // Skip authentication for static files or when manager is missing
        String uri = request.getRequestURI();
        String path = uri.substring(request.getContextPath().length());
        if (PathUtil.isStatic(path) || sessionManager == null) {
            chain.doFilter(req, resp);
            return;
        }

        // Prepare authentication data
        RequestInfoDTO reqInfo = AuthWebMapper.toReqInfoDTO(request);
        AuthContext currentContext = sessionManager.getContext(request);

        // Run request handling logic (validate / restore / rotate)
        AuthResult<AuthStatus, AuthContext> result =
                authReqService.handleRequest(reqInfo, currentContext);

        // Perform action based on authentication status
        switch (result.getStatus()) {
            case SESSION_ROTATED: {
                // Token and session ID rotated → update session & cookies
                AuthContext newContext = result.getData();
                sessionManager.refreshSession(request, response, newContext);
                break;
            }
            case SESSION_RESTORED: {
                // No valid session → restore a new one from cookies
                AuthContext restoredCtx = result.getData();
                sessionManager.startSession(request, response, restoredCtx);
                break;
            }
            case SESSION_INVALID: {
                // Invalid cookies or expired tokens → force logout
                sessionManager.endSession(request, response);
                break;
            }
            case SESSION_REUSED, SESSION_NOT_FOUND:
            default: {
                // Nothing to update → continue request
            }
        }

        chain.doFilter(req, resp);
    }
}