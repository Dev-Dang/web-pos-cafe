package com.laptrinhweb.zerostarcafe.web.auth.servlet;

import com.laptrinhweb.zerostarcafe.core.utils.Flash;
import com.laptrinhweb.zerostarcafe.core.validation.ValidationResult;
import com.laptrinhweb.zerostarcafe.domain.auth.dto.RegisterDTO;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthResult;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthStatus;
import com.laptrinhweb.zerostarcafe.domain.auth.service.AuthService;
import com.laptrinhweb.zerostarcafe.web.common.routing.AppRoute;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles user register: validate → register.
 *
 * @author Dang Van Trung
 * @version 1.0.1
 * @lastModified 13/12/2025
 * @since 1.0.0
 */
@WebServlet(name = "RegisterServlet", urlPatterns = "/auth/register")
public class RegisterServlet extends HttpServlet {

    private final AuthService authService = new AuthService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Read registration form data
        String email = req.getParameter("regEmail");
        String username = req.getParameter("regUsername");
        String password = req.getParameter("regPassword");
        String agreeTerms = req.getParameter("agreeTerms");

        RegisterDTO form = new RegisterDTO(email, username, password, agreeTerms);

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

        // Handle known business errors (duplicate email / username)
        Map<String, String> fieldErrors = new HashMap<>();
        if (authResult.getStatus() == AuthStatus.EMAIL_EXISTS)
            fieldErrors.put("regEmail", "form.email_exists");
        if (authResult.getStatus() == AuthStatus.USERNAME_EXISTS)
            fieldErrors.put("regUsername", "form.username_exists");

        failedRegister(form, fieldErrors, req, resp);
    }

    private void successRegister(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        // Show success message
        Flash flash = new Flash(req);
        flash.success("message.register_success").send();

        // Redirect to a safe default page
        AppRoute.HOME.redirect(req, resp);
    }

    private void failedRegister(RegisterDTO form,
                                Map<String, String> fieldErrors,
                                HttpServletRequest req,
                                HttpServletResponse resp) throws IOException {

        // Show fail message
        Flash flash = new Flash(req);
        flash.error("message.register_failed")
                .formResponse(form.formState(), fieldErrors)
                .set("openModal", "register")
                .send();

        // Always redirect to the client home page for safe
        AppRoute.HOME.redirect(req, resp);
    }
}