package com.laptrinhweb.zerostarcafe.web.auth.servlet;

import com.laptrinhweb.zerostarcafe.core.mail.SmtpMailService;
import com.laptrinhweb.zerostarcafe.core.security.SecurityKeys;
import com.laptrinhweb.zerostarcafe.core.security.TokenUtils;
import com.laptrinhweb.zerostarcafe.core.utils.LoggerUtil;
import com.laptrinhweb.zerostarcafe.core.validation.ValidationResult;
import com.laptrinhweb.zerostarcafe.domain.auth.dto.ForgotPasswordDTO;
import com.laptrinhweb.zerostarcafe.domain.auth.dto.RequestInfoDTO;
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
import com.laptrinhweb.zerostarcafe.web.common.view.View;
import com.laptrinhweb.zerostarcafe.web.common.view.ViewMap;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Handles forgot-password requests and sends reset link via email.
 */
@WebServlet(name = "ForgotPasswordServlet", urlPatterns = {RouteMap.FORGOT_PASSWORD})
public class ForgotPasswordServlet extends HttpServlet {

    private final UserService userService = UserService.getInstance();
    private final PasswordResetTokenService resetTokenService =
            PasswordResetTokenService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Set auto-open modal flag in response context
        RespContext.from(req).setData(
                WebConstants.Flag.RE_OPEN_MODAL,
                WebConstants.Auth.FORGOT_PASSWORD_MODAL
        );

        // Forward to home page to render full page with modal
        AppRoute.forward(RouteMap.HOME, req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        ForgotPasswordDTO form = AuthWebMapper.toForgotPasswordDTO(req);

        ValidationResult validation = form.validate();
        if (!validation.valid()) {
            Message.error(req, "message.validation_failed");
            failedForm(form, validation.fieldErrors(), req, resp);
            return;
        }

        RequestInfoDTO reqInfo = AuthWebMapper.toReqInfoDTO(req);
        String email = normalize(form.getEmail());
        User user = userService.getActiveByEmail(email);

        if (user != null) {
            String rawToken = TokenUtils.generateToken();
            String tokenHash = TokenUtils.hashToken(rawToken);
            LocalDateTime now = LocalDateTime.now();

            PasswordResetToken token = new PasswordResetToken();
            token.setUserId(user.getId());
            token.setTokenHash(tokenHash);
            token.setExpiresAt(now.plusMinutes(SecurityKeys.RESET_PASSWORD_TOKEN_TTL_MINUTES));
            token.setCreatedAt(now);
            if (reqInfo != null) {
                token.setRequestIp(reqInfo.getIpAddress());
                token.setUserAgent(reqInfo.getUserAgent());
            }

            resetTokenService.save(user.getId(), token);

            try {
                String resetLink = buildResetLink(req, rawToken);
                String html = buildResetEmailHtml(resetLink);
                SmtpMailService.from(req.getServletContext())
                        .sendHtml(user.getEmail(), "Reset your password", html);
            } catch (Exception e) {
                LoggerUtil.error(ForgotPasswordServlet.class,
                        "Failed to send reset password email.", e);
                Message.error(req, "message.action_failed");
                failedForm(form, Map.of(), req, resp);
                return;
            }
        }

        Message.success(req, "message.password_reset_sent");
        AppRoute.redirect(RouteMap.HOME, req, resp);
    }

    private void failedForm(
            ForgotPasswordDTO form,
            Map<String, String> fieldErrors,
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws IOException, ServletException {
        req.setAttribute(WebConstants.Attribute.FORM_DATA, form.formState());

        if (fieldErrors != null && !fieldErrors.isEmpty()) {
            req.setAttribute(WebConstants.Attribute.FORM_ERRORS, fieldErrors);
        }

        resp.setStatus(HttpServletResponse.SC_UNPROCESSABLE_CONTENT);
        View.render(ViewMap.Client.FORGOT_PASSWORD_FORM, req, resp);
    }

    private String buildResetLink(HttpServletRequest req, String rawToken) {
        String scheme = req.getScheme();
        String host = req.getServerName();
        int port = req.getServerPort();

        StringBuilder base = new StringBuilder();
        base.append(scheme).append("://").append(host);

        boolean defaultPort = ("http".equalsIgnoreCase(scheme) && port == 80)
                || ("https".equalsIgnoreCase(scheme) && port == 443);
        if (!defaultPort) {
            base.append(":").append(port);
        }

        String tokenParam = URLEncoder.encode(rawToken, StandardCharsets.UTF_8);
        base.append(req.getContextPath())
                .append(RouteMap.RESET_PASSWORD)
                .append("?" + WebConstants.Auth.TOKEN + "=")
                .append(tokenParam);

        return base.toString();
    }

    private String buildResetEmailHtml(String resetLink) {
        return """
                <p>You requested a password reset.</p>
                <p><a href="%s">Click here to reset your password</a></p>
                <p>This link will expire in %d minutes.</p>
                """.formatted(resetLink, SecurityKeys.RESET_PASSWORD_TOKEN_TTL_MINUTES);
    }

    private static String normalize(String s) {
        return s == null ? null : s.trim().toLowerCase();
    }
}
