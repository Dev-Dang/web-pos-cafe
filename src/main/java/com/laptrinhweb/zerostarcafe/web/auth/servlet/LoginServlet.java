package com.laptrinhweb.zerostarcafe.web.auth.servlet;

import com.laptrinhweb.zerostarcafe.core.security.SecurityKeys;
import com.laptrinhweb.zerostarcafe.core.utils.ContextUtil;
import com.laptrinhweb.zerostarcafe.core.utils.Message;
import com.laptrinhweb.zerostarcafe.core.utils.LoggerUtil;
import com.laptrinhweb.zerostarcafe.core.validation.ValidationResult;
import com.laptrinhweb.zerostarcafe.domain.auth.dto.LoginDTO;
import com.laptrinhweb.zerostarcafe.domain.auth.dto.RequestInfoDTO;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthContext;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthResult;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthStatus;
import com.laptrinhweb.zerostarcafe.domain.auth.service.AuthService;
import com.laptrinhweb.zerostarcafe.domain.user.model.UserRole;
import com.laptrinhweb.zerostarcafe.web.auth.mapper.AuthWebMapper;
import com.laptrinhweb.zerostarcafe.web.auth.session.AuthSessionManager;
import com.laptrinhweb.zerostarcafe.web.common.routing.AppRoute;
import com.laptrinhweb.zerostarcafe.web.common.filters.UnpolyFilter;
import com.laptrinhweb.zerostarcafe.web.common.view.View;
import com.laptrinhweb.zerostarcafe.web.common.view.ViewArea;
import com.laptrinhweb.zerostarcafe.web.common.view.ViewMap;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

/**
 * Handles user login: validate → authenticate → create session → issue cookies.
 *
 * @author Dang Van Trung
 * @version 1.0.3
 * @lastModified 28/12/2025
 * @since 1.0.0
 */
@WebServlet(name = "LoginServlet", urlPatterns = "/auth/login")
public class LoginServlet extends HttpServlet {

    private AuthSessionManager sessionManager;
    private final AuthService authService = new AuthService();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext ctx = config.getServletContext();

        this.sessionManager = ContextUtil.require(
                ctx, SecurityKeys.CTX_AUTH_SESSION_MANAGER, AuthSessionManager.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // If this is a direct browser access (not Unpoly), render full page with modal
        // This handles the case where user reloads on the modal fragment URL
        if (!isUnpoly(req)) {
            // Render as full page with modal pre-opened
            req.setAttribute("openModal", "_login");
            View homeView = View.getPage(ViewArea.CLIENT, "/home");
            View.render(homeView, req, resp);
            return;
        }

        // For Unpoly requests, serve only the login form fragment
        View.render(ViewMap.Client.Form.LOGIN, req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Read login form data
        LoginDTO form = AuthWebMapper.toLoginDTO(req);

        // Validate input - always validate first
        ValidationResult validation = form.validate();
        if (!validation.valid()) {
            LoggerUtil.info(getClass(), "Validation failed: " + validation.fieldErrors());
            failedLogin(req, resp, form, validation);
            return;
        }

        // Build request metadata (IP, User-Agent, cookies, ...)
        RequestInfoDTO reqInfo = AuthWebMapper.toReqInfoDTO(req);

        // Perform authentication
        AuthResult<AuthStatus, AuthContext> result =
                authService.authenticate(form, reqInfo);

        AuthContext context = result.getData();
        if (context == null || !result.isSuccess()) {
            LoggerUtil.info(getClass(), "Authentication failed");
            // Create a fake validation error for authentication failure
            ValidationResult authValidation = ValidationResult.fail("email", "message.invalid_credentials");
            failedLogin(req, resp, form, authValidation);
            return;
        }

        // Authentication successful → create session and redirect
        LoggerUtil.info(getClass(), "Authentication successful");
        successLogin(req, resp, context);
    }

    private boolean isUnpoly(HttpServletRequest req) {
        return UnpolyFilter.isUnpoly(req);
    }

    private void successLogin(HttpServletRequest req,
                              HttpServletResponse resp,
                              AuthContext context) throws IOException, ServletException {

        // Create session and persist authentication context
        sessionManager.startSession(req, resp, context);

        // Set flag to trigger cart merge on next page load
        req.getSession().setAttribute("needsCartMerge", Boolean.TRUE);

        String fallback = AppRoute.HOME.getUrl(req);
        String target = getRedirectPath(context, req, fallback);

        if (isUnpoly(req)) {
            // For Unpoly: return redirect header to navigate to home
            // Unpoly will handle modal closure and navigation
            resp.setHeader("X-Up-Location", target);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("text/html; charset=UTF-8");
            
            // Send empty response body - X-Up-Location header handles navigation
            resp.getWriter().write("");
            return;
        }

        // Traditional flow: just redirect
        resp.sendRedirect(target);
    }

    private void failedLogin(HttpServletRequest req,
                             HttpServletResponse resp,
                             LoginDTO form,
                             ValidationResult validation) throws IOException, ServletException {

        LoggerUtil.info(getClass(), "failedLogin called, X-Up-Version header: " + req.getHeader("X-Up-Version"));
        LoggerUtil.info(getClass(), "isUnpoly: " + isUnpoly(req));

        if (isUnpoly(req)) {
            LoggerUtil.info(getClass(), "Handling Unpoly request - returning 422 with form");
            // For Unpoly: return updated form with errors (422 status)
            resp.setStatus(HttpServletResponse.SC_UNPROCESSABLE_CONTENT);

            // Add form data for refill (as Map for JSP iteration)
            req.setAttribute("formData", form.formState());

            // Add validation errors
            if (validation != null && !validation.valid()) {
                req.setAttribute("formErrors", validation.fieldErrors());
                LoggerUtil.info(getClass(), "Validation errors: " + validation.fieldErrors());
            }

            // Add error message using simple Message system
            Message.error(req, "message.login_failed");

            // Return the complete login form
            View.render(ViewMap.Client.Form.LOGIN, req, resp);
            return;
        }

        LoggerUtil.info(getClass(), "Handling non-Unpoly request - redirecting to home");
        // Traditional non-Unpoly flow: just redirect (no Flash/PRG)
        AppRoute.HOME.redirect(req, resp);
    }

    private String getRedirectPath(AuthContext ctx,
                                   HttpServletRequest req,
                                   String fallback) {

        if (ctx == null || ctx.getAuthUser() == null)
            return fallback;

        var user = ctx.getAuthUser();
        if (user.hasRole(UserRole.SUPER_ADMIN) || user.hasRole(UserRole.STORE_MANAGER))
            return AppRoute.DASHBOARD.getUrl(req);

        // Normal user
        return fallback;
    }
}
