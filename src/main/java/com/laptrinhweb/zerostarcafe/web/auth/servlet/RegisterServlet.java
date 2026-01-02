package com.laptrinhweb.zerostarcafe.web.auth.servlet;

import com.laptrinhweb.zerostarcafe.core.utils.Message;
import com.laptrinhweb.zerostarcafe.core.validation.ValidationResult;
import com.laptrinhweb.zerostarcafe.domain.auth.dto.RegisterDTO;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthResult;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthStatus;
import com.laptrinhweb.zerostarcafe.domain.auth.service.AuthService;
import com.laptrinhweb.zerostarcafe.web.auth.mapper.AuthWebMapper;
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
import java.util.HashMap;
import java.util.Map;

/**
 * Handles user registration: validate → register.
 *
 * @author Dang Van Trung
 * @version 2.0.0
 * @lastModified 02/01/2026
 * @since 1.0.0
 */
@WebServlet(name = "RegisterServlet", urlPatterns = "/auth/register")
public class RegisterServlet extends HttpServlet {

    private final AuthService authService = new AuthService();

    // Note: doGet() removed - use PartialServlet at /partial/register-form instead

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Read registration form data and convert to DTO
        RegisterDTO form = AuthWebMapper.toRegisterDTO(req);

        // Validate input
        ValidationResult validation = form.validate();
        if (!validation.valid()) {
            failedRegister(form, validation.fieldErrors(), req, resp);
            return;
        }

        // Perform registration
        AuthResult<AuthStatus, Void> authResult = authService.register(form);

        if (authResult.isSuccess()) {
            successRegister(req, resp);
            return;
        }

        // Handle known business errors (duplicate email)
        Map<String, String> fieldErrors = new HashMap<>();
        if (authResult.getStatus() == AuthStatus.EMAIL_EXISTS)
            fieldErrors.put("email", "form.email_exists");

        failedRegister(form, fieldErrors, req, resp);
    }

    private void successRegister(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        // Smart redirect - handles both partial and normal requests
        AppRoute.redirect(RouteMap.HOME, req, resp);
    }

    private void failedRegister(RegisterDTO form,
                                Map<String, String> fieldErrors,
                                HttpServletRequest req,
                                HttpServletResponse resp) throws IOException, ServletException {

        // Return updated form with errors (422 status)
        resp.setStatus(HttpServletResponse.SC_UNPROCESSABLE_CONTENT);

        // Add form data for refill
        req.setAttribute("formData", form.formState());

        // Add validation errors
        if (fieldErrors != null && !fieldErrors.isEmpty()) {
            req.setAttribute("formErrors", fieldErrors);
        }

        // Add error message
        Message.error(req, "message.register_failed");

        // Return the complete register form
        View.render(ViewMap.Client.REGISTER_FORM, req, resp);
    }
}