package com.laptrinhweb.zerostarcafe.web.auth.servlet;

import com.laptrinhweb.zerostarcafe.core.security.PasswordUtils;
import com.laptrinhweb.zerostarcafe.core.utils.LoggerUtil;
import com.laptrinhweb.zerostarcafe.core.validation.ValidationResult;
import com.laptrinhweb.zerostarcafe.domain.auth.dto.ResetPasswordDTO;
import com.laptrinhweb.zerostarcafe.domain.auth.reset.PasswordResetToken;
import com.laptrinhweb.zerostarcafe.domain.auth.reset.PasswordResetTokenService;
import com.laptrinhweb.zerostarcafe.domain.user.model.User;
import com.laptrinhweb.zerostarcafe.domain.user.service.UserService;
import com.laptrinhweb.zerostarcafe.web.auth.mapper.AuthWebMapper;
import com.laptrinhweb.zerostarcafe.web.common.WebConstants;
import com.laptrinhweb.zerostarcafe.web.common.response.Message;
import com.laptrinhweb.zerostarcafe.web.common.response.RespContext;
import com.laptrinhweb.zerostarcafe.web.common.routing.AppRoute;
import com.laptrinhweb.zerostarcafe.web.common.routing.RouteMap;
import com.laptrinhweb.zerostarcafe.web.common.utils.RequestUtils;
import com.laptrinhweb.zerostarcafe.web.common.view.View;
import com.laptrinhweb.zerostarcafe.web.common.view.ViewMap;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

/**
 * Handles reset-password token validation and password update.
 */
@WebServlet(name = "ResetPasswordServlet", urlPatterns = {RouteMap.RESET_PASSWORD + "/*"})
public class ResetPasswordServlet extends HttpServlet {

    private final UserService userService = UserService.getInstance();
    private final PasswordResetTokenService resetTokenService =
            PasswordResetTokenService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Validate reset token from request parameter
        String token = RequestUtils.getStringParam(req, WebConstants.Auth.TOKEN);
        if (!isValidToken(token)) {
            Message.error(req, "message.password_reset_invalid");
            AppRoute.redirect(RouteMap.HOME, req, resp);
            return;
        }

        // Set auto-open modal flag in response context
        RespContext.from(req)
                .setData(WebConstants.Flag.RE_OPEN_MODAL,
                        WebConstants.Auth.RESET_PASSWORD_MODAL)
                .setData(WebConstants.Auth.RESET_PASSWORD_TOKEN, token);

        // Forward to home page to render full page with modal
        AppRoute.forward(RouteMap.HOME, req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        ResetPasswordDTO form = AuthWebMapper.toResetPasswordDTO(req);
        ValidationResult validation = form.validate();
        if (!validation.valid()) {
            Message.error(req, "message.validation_failed");
            failedForm(validation.fieldErrors(), req, resp);
            return;
        }

        String token = form.getToken();
        Optional<PasswordResetToken> tokenOpt = resetTokenService.findValidByRawToken(token);
        if (tokenOpt.isEmpty()) {
            Message.error(req, "message.password_reset_invalid");
            AppRoute.redirect(RouteMap.HOME, req, resp);
            return;
        }

        PasswordResetToken resetToken = tokenOpt.get();
        User user = userService.getActiveById(resetToken.getUserId());
        if (user == null) {
            Message.error(req, "message.password_reset_invalid");
            AppRoute.redirect(RouteMap.HOME, req, resp);
            return;
        }

        try {
            user.setPasswordHash(PasswordUtils.hash(form.getPassword()));
            userService.save(user);
            resetTokenService.markUsedByRawToken(token);

            Message.success(req, "message.password_changed");
            AppRoute.redirect(RouteMap.HOME, req, resp);
        } catch (Exception e) {
            LoggerUtil.error(ResetPasswordServlet.class,
                    "Failed to reset password.", e);
            Message.error(req, "message.action_failed");
            failedForm(Map.of(), req, resp);
        }
    }

    private void failedForm(
            Map<String, String> fieldErrors,
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws IOException, ServletException {
        if (fieldErrors != null && !fieldErrors.isEmpty()) {
            req.setAttribute(WebConstants.Attribute.FORM_ERRORS, fieldErrors);
        }

        resp.setStatus(HttpServletResponse.SC_UNPROCESSABLE_CONTENT);
        View.render(ViewMap.Client.RESET_PASSWORD_FORM, req, resp);
    }

    private boolean isValidToken(String rawToken) {
        return resetTokenService.findValidByRawToken(rawToken).isPresent();
    }
}
