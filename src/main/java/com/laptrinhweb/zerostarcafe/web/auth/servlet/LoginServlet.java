package com.laptrinhweb.zerostarcafe.web.auth.servlet;

import com.laptrinhweb.zerostarcafe.core.security.SecurityKeys;
import com.laptrinhweb.zerostarcafe.core.utils.ContextUtil;
import com.laptrinhweb.zerostarcafe.core.utils.LoggerUtil;
import com.laptrinhweb.zerostarcafe.core.utils.Message;
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
import com.laptrinhweb.zerostarcafe.web.common.routing.RouteMap;
import com.laptrinhweb.zerostarcafe.web.common.view.View;
import com.laptrinhweb.zerostarcafe.web.common.view.ViewMap;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Handles user login: validate → authenticate → create session → issue cookies.
 *
 * @author Dang Van Trung
 * @version 2.0.0
 * @lastModified 02/01/2026
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

    // Note: doGet() removed - use PartialServlet at /partial/login-form instead

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

    private void successLogin(HttpServletRequest req,
                              HttpServletResponse resp,
                              AuthContext context) throws IOException, ServletException {

        // Create session and persist authentication context
        sessionManager.startSession(req, resp, context);

        // Set flag to trigger cart merge on next page load
        req.getSession().setAttribute("needsCartMerge", Boolean.TRUE);

        // Get redirect path (without context path)
        String redirectPath = getRedirectPath(context);

        // Smart redirect - handles both partial and normal requests
        Message.success(req, "message.login_success");
        AppRoute.redirect(redirectPath, req, resp);
    }

    private void failedLogin(HttpServletRequest req,
                             HttpServletResponse resp,
                             LoginDTO form,
                             ValidationResult validation) throws IOException, ServletException {

        LoggerUtil.info(getClass(), "failedLogin - returning form with errors");

        // Return updated form with errors (422 status)
        resp.setStatus(HttpServletResponse.SC_UNPROCESSABLE_CONTENT);

        // Add form data for refill
        req.setAttribute("formData", form.formState());

        // Add validation errors
        if (validation != null && !validation.valid()) {
            req.setAttribute("formErrors", validation.fieldErrors());
            LoggerUtil.info(getClass(), "Validation errors: " + validation.fieldErrors());
        }

        // Add error message
        Message.error(req, "message.login_failed");

        // Return the complete login form
        View.render(ViewMap.Client.LOGIN_FORM, req, resp);
    }

    private String getRedirectPath(AuthContext ctx) {
        if (ctx == null || ctx.getAuthUser() == null)
            return RouteMap.HOME;

        var user = ctx.getAuthUser();
        if (user.hasRole(UserRole.SUPER_ADMIN) || user.hasRole(UserRole.STORE_MANAGER))
            return RouteMap.DASHBOARD;

        // Normal user
        return RouteMap.HOME;
    }
}
