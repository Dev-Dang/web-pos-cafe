package com.laptrinhweb.zerostarcafe.web.auth.servlet;

import com.laptrinhweb.zerostarcafe.core.utils.Message;
import com.laptrinhweb.zerostarcafe.core.validation.ValidationResult;
import com.laptrinhweb.zerostarcafe.domain.auth.dto.RegisterDTO;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthResult;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthStatus;
import com.laptrinhweb.zerostarcafe.domain.auth.service.AuthService;
import com.laptrinhweb.zerostarcafe.web.auth.mapper.AuthWebMapper;
import com.laptrinhweb.zerostarcafe.web.common.filters.UnpolyFilter;
import com.laptrinhweb.zerostarcafe.web.common.routing.AppRoute;
import com.laptrinhweb.zerostarcafe.web.common.view.View;
import com.laptrinhweb.zerostarcafe.web.common.view.ViewArea;
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
 * Handles user register: validate → register.
 *
 * @author Dang Van Trung
 * @version 1.0.2
 * @lastModified 28/12/2025
 * @since 1.0.0
 */
@WebServlet(name = "RegisterServlet", urlPatterns = "/auth/register")
public class RegisterServlet extends HttpServlet {

    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Direct browser access: render full page with modal pre-opened
        if (!isUnpoly(req)) {
            req.setAttribute("openModal", "_register");
            View homeView = View.getPage(ViewArea.CLIENT, "/home");
            View.render(homeView, req, resp);
            return;
        }

        // Unpoly request: render only the form fragment
        View.render(ViewMap.Client.Form.REGISTER, req, resp);
    }

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

    private boolean isUnpoly(HttpServletRequest req) {
        return UnpolyFilter.isUnpoly(req);
    }

    private void successRegister(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        if (isUnpoly(req)) {
            // For Unpoly: don't add message here (it's lost on navigation)
            // The home page will render with a success message via sessionStorage
            
            // Tell Unpoly to navigate to target page
            String target = AppRoute.HOME.getUrl(req);
            resp.setHeader("X-Up-Location", target);
            resp.setHeader("X-Up-Mode", "replace");
            resp.setStatus(HttpServletResponse.SC_OK);
            
            // Send a marker so client knows to show success message
            resp.setContentType("text/html; charset=UTF-8");
            resp.getWriter().write("<script>sessionStorage.setItem('registerSuccess', 'true');</script>");
            return;
        }

        // Traditional flow: just redirect (no Flash/PRG anymore)
        AppRoute.HOME.redirect(req, resp);
    }

    private void failedRegister(RegisterDTO form,
                                Map<String, String> fieldErrors,
                                HttpServletRequest req,
                                HttpServletResponse resp) throws IOException, ServletException {

        if (isUnpoly(req)) {
            // For Unpoly: return updated form with errors (422 status)
            resp.setStatus(HttpServletResponse.SC_UNPROCESSABLE_CONTENT);

            // Add form data for refill
            req.setAttribute("formData", form.formState());

            // Add validation errors
            if (fieldErrors != null && !fieldErrors.isEmpty()) {
                req.setAttribute("formErrors", fieldErrors);
            }

            // Add error message using simple Message system
            Message.error(req, "message.register_failed");

            // Return the complete register form
            View.render(ViewMap.Client.Form.REGISTER, req, resp);
            return;
        }

        // Traditional flow: just redirect (no Flash/PRG anymore)
        AppRoute.HOME.redirect(req, resp);
    }
}