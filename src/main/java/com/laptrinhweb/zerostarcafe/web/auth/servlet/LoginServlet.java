package com.laptrinhweb.zerostarcafe.web.auth.servlet;

import com.laptrinhweb.zerostarcafe.core.security.SecurityKeys;
import com.laptrinhweb.zerostarcafe.core.utils.ContextUtil;
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
import com.laptrinhweb.zerostarcafe.web.common.WebConstants;
import com.laptrinhweb.zerostarcafe.web.common.response.Message;
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
import java.util.HashMap;
import java.util.Map;

/**
 * Handles user login: validate → authenticate → create session → issue cookies.
 *
 * @author Dang Van Trung
 * @version 1.0.1
 * @lastModified 02/01/2026
 * @since 1.0.0
 */
@WebServlet(name = "LoginServlet", urlPatterns = "/auth/login")
public class LoginServlet extends HttpServlet {

    private AuthSessionManager sessionManager;
    private final AuthService authService = AuthService.getInstance();

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

        View.render(ViewMap.Client.LOGIN_FORM, req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Read login form data
        LoginDTO form = AuthWebMapper.toLoginDTO(req);

        // Validate input
        ValidationResult validation = form.validate();
        if (!validation.valid()) {
            Message.error(req, "message.validation_failed");
            failedLogin(form, validation.fieldErrors(), req, resp);
            return;
        }

        // Build request metadata (IP, User-Agent, cookies, ...)
        RequestInfoDTO reqInfo = AuthWebMapper.toReqInfoDTO(req);

        // Perform authentication
        AuthResult<AuthStatus, AuthContext> result =
                authService.authenticate(form, reqInfo);

        AuthContext context = result.getData();
        if (result.isSuccess() && context != null) {
            // Authentication successful → create session and redirect
            Message.success(req, "message.login_success");
            successLogin(context, req, resp);
            return;
        }

        // Handle known authentication errors
        Map<String, String> fieldErrors = new HashMap<>();
        if (result.getStatus() == AuthStatus.INVALID_CREDENTIALS) {
            Message.error(req, "message.invalid_credentials");
            fieldErrors.put("email", "form.invalid_credentials");
            failedLogin(form, fieldErrors, req, resp);
            return;
        }

        // Handle unknown errors
        Message.error(req, "message.login_failed");
        failedLogin(form, fieldErrors, req, resp);
    }

    private void successLogin(AuthContext context,
                              HttpServletRequest req,
                              HttpServletResponse resp) throws IOException {

        // Create session and persist authentication context
        sessionManager.startSession(req, resp, context);

        // Set flag to trigger cart merge on next page load
        req.getSession().setAttribute(WebConstants.Attribute.NEED_CART_MERGE, Boolean.TRUE);

        // Redirect user to appropriate page
        String redirectPath = getRedirectPath(context);
        AppRoute.redirect(redirectPath, req, resp);
    }

    private void failedLogin(LoginDTO form,
                             Map<String, String> fieldErrors,
                             HttpServletRequest req,
                             HttpServletResponse resp) throws IOException, ServletException {

        // Add form data for refill (only email)
        req.setAttribute(WebConstants.Attribute.FORM_DATA, form.formState());

        // Add validation errors
        if (fieldErrors != null && !fieldErrors.isEmpty()) {
            req.setAttribute(WebConstants.Attribute.FORM_ERRORS, fieldErrors);
        }

        // Return updated form with errors (422 status)
        resp.setStatus(HttpServletResponse.SC_UNPROCESSABLE_CONTENT);
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
