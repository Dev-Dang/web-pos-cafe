package com.laptrinhweb.zerostarcafe.web.auth.servlet;

import com.laptrinhweb.zerostarcafe.core.validation.ValidationResult;
import com.laptrinhweb.zerostarcafe.domain.auth.dto.RegisterDTO;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthResult;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthStatus;
import com.laptrinhweb.zerostarcafe.domain.auth.service.AuthService;
import com.laptrinhweb.zerostarcafe.web.auth.mapper.AuthWebMapper;
import com.laptrinhweb.zerostarcafe.web.common.response.Message;
import com.laptrinhweb.zerostarcafe.web.common.routing.AppRoute;
import com.laptrinhweb.zerostarcafe.web.common.routing.RouteMap;
import com.laptrinhweb.zerostarcafe.web.common.utils.WebConstants;
import com.laptrinhweb.zerostarcafe.web.common.view.View;
import com.laptrinhweb.zerostarcafe.web.common.view.ViewMap;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles user registration: validate → register.
 *
 * @author Dang Van Trung
 * @version 1.0.1
 * @lastModified 02/01/2026
 * @since 1.0.0
 */
@WebServlet(name = "RegisterServlet", urlPatterns = "/auth/register")
public class RegisterServlet extends HttpServlet {

    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        View.render(ViewMap.Client.REGISTER_FORM, req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Read form data
        RegisterDTO form = AuthWebMapper.toRegisterDTO(req);

        // Validate input
        ValidationResult validation = form.validate();
        if (!validation.valid()) {
            Message.error(req, "message.validation_failed");
            failedRegister(form, validation.fieldErrors(), req, resp);
            return;
        }

        // Perform registration
        AuthResult<AuthStatus, Void> authResult = authService.register(form);

        if (authResult.isSuccess()) {
            Message.success(req, "message.register_success");
            AppRoute.redirect(RouteMap.HOME, req, resp);
            return;
        }

        // Handle known business errors (duplicate email)
        Map<String, String> fieldErrors = new HashMap<>();
        if (authResult.getStatus() == AuthStatus.EMAIL_EXISTS) {
            Message.error(req, "message.email_exists");
            fieldErrors.put("email", "form.email_exists");
            failedRegister(form, fieldErrors, req, resp);
            return;
        }

        // Handle unknown errors
        Message.error(req, "message.register_failed");
        failedRegister(form, fieldErrors, req, resp);
    }

    private void failedRegister(RegisterDTO form,
                                Map<String, String> fieldErrors,
                                HttpServletRequest req,
                                HttpServletResponse resp) throws IOException, ServletException {

        // Add form data for refill (only email)
        req.setAttribute(WebConstants.Request.FORM_DATA, form.formState());

        // Add validation errors
        if (fieldErrors != null && !fieldErrors.isEmpty()) {
            req.setAttribute(WebConstants.Request.FORM_ERRORS, fieldErrors);
        }

        // Return updated form with errors (422 status)
        resp.setStatus(HttpServletResponse.SC_UNPROCESSABLE_CONTENT);
        View.render(ViewMap.Client.REGISTER_FORM, req, resp);
    }
}