package com.laptrinhweb.zerostarcafe.web.auth.servlet;

import com.laptrinhweb.zerostarcafe.core.security.SecurityKeys;
import com.laptrinhweb.zerostarcafe.core.utils.ContextUtil;
import com.laptrinhweb.zerostarcafe.core.utils.Flash;
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
import java.util.List;
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

        // Serve the login form (for modals and direct access)
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
        return req.getHeader("X-Up-Version") != null;
    }

    private void successLogin(HttpServletRequest req,
                              HttpServletResponse resp,
                              AuthContext context) throws IOException, ServletException {

        // Create session and persist authentication context
        sessionManager.startSession(req, resp, context);

        // Set flag to trigger cart merge on next page load
        req.getSession().setAttribute("needsCartMerge", Boolean.TRUE);

        if (isUnpoly(req)) {
            // For Unpoly: close modal and redirect
            // Use X-Up-Location to tell Unpoly where to navigate after success
            resp.setHeader("X-Up-Location", AppRoute.HOME.getUrl(req));
            
            // Add translated success message to request scope
            req.setAttribute("messages", List.of(
                    new Flash.Message(Flash.MsgType.success, "message.login_success")
            ));

            // Return flash-data fragment with translated messages
            View.render(ViewMap.Client.Fragment.FLASH_CONTAINER, req, resp);
            return;
        }

        // Traditional flow: use Flash and redirect
        Flash flash = new Flash(req);
        flash.success("message.login_success").send();

        // Redirect user to appropriate page
        String fallback = AppRoute.HOME.getUrl(req);
        String target = getRedirectPath(context, req, fallback);
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

            // Add error message
            req.setAttribute("messages", List.of(
                    new Flash.Message(Flash.MsgType.error, "message.login_failed")
            ));

            // Return the complete login form
            View.render(ViewMap.Client.Form.LOGIN, req, resp);
            return;
        }

        LoggerUtil.info(getClass(), "Handling non-Unpoly request - PRG flow");
        // Traditional PRG flow
        Flash flash = new Flash(req);
        flash.error("message.login_failed")
                .formResponse(form.formState(), validation != null ? validation.fieldErrors() : Map.of())
                .set("openModal", "_login")
                .send();

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