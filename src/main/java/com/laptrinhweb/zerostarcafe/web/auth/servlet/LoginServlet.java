package com.laptrinhweb.zerostarcafe.web.auth.servlet;

import com.laptrinhweb.zerostarcafe.core.security.SecurityKeys;
import com.laptrinhweb.zerostarcafe.core.utils.ContextUtil;
import com.laptrinhweb.zerostarcafe.core.utils.Flash;
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
 * @version 1.0.2
 * @lastModified 13/12/2025
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Read login form data
        String username = req.getParameter("loginUsername");
        String password = req.getParameter("loginPassword");

        LoginDTO form = new LoginDTO(username, password);

        // Validate input
        ValidationResult validation = form.validate();
        if (!validation.valid()) {
            failedLogin(req, resp, form);
            return;
        }

        // Build request metadata (IP, User-Agent, cookies, ...)
        RequestInfoDTO reqInfo = AuthWebMapper.toReqInfoDTO(req);

        // Perform authentication
        AuthResult<AuthStatus, AuthContext> result =
                authService.authenticate(form, reqInfo);

        AuthContext context = result.getData();
        if (context == null || !result.isSuccess()) {
            failedLogin(req, resp, form);
            return;
        }

        // Authentication successful → create session and redirect
        successLogin(req, resp, context);
    }

    private void successLogin(HttpServletRequest req,
                              HttpServletResponse resp,
                              AuthContext context) throws IOException {

        // Create session and persist authentication context
        sessionManager.startSession(req, resp, context);

        // Show success message
        Flash flash = new Flash(req);
        flash.success("message.login_success").send();

        // Redirect user to appropriate page
        String fallback = AppRoute.HOME.getUrl(req);
        String target = getRedirectPath(context, req, fallback);
        resp.sendRedirect(target);
    }

    private void failedLogin(HttpServletRequest req,
                             HttpServletResponse resp,
                             LoginDTO form) throws IOException {
        // Show fail message
        Flash flash = new Flash(req);
        flash.error("message.login_failed")
                .formResponse(form.formState(), Map.of())
                .set("openModal", "login")
                .send();

        // Always redirect to the client home page for safe
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