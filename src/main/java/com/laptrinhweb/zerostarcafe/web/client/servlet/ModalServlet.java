package com.laptrinhweb.zerostarcafe.web.client.servlet;

import com.laptrinhweb.zerostarcafe.core.utils.PathUtil;
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

/**
 * Handles dynamic modal loading requests from the client-side JavaScript.
 * Maps modal names to predefined views in ViewMap for type safety.
 *
 * @author Dang Van Trung
 * @version 2.0.0
 * @lastModified 02/01/2026
 * @since 1.0.0
 */
@WebServlet(name = "ModalServlet", urlPatterns = "/modals")
public class ModalServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String modalName = req.getParameter("name");
        if (modalName == null || modalName.isBlank()) {
            AppRoute.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Missing modal name", resp);
            return;
        }

        // Map modal name to View
        View view = resolveModalView(modalName);
        if (view == null) {
            AppRoute.sendError(HttpServletResponse.SC_NOT_FOUND,
                    "Modal not found: " + modalName, resp);
            return;
        }

        View.render(view, req, resp);
    }

    /**
     * Resolves modal name to a View from ViewMap.
     * This ensures all modal views are registered and type-safe.
     */
    private View resolveModalView(String modalName) {
        return switch (modalName.toLowerCase()) {
            case "login", "_login" -> ViewMap.Client.LOGIN_FORM;
            case "register", "_register" -> ViewMap.Client.REGISTER_FORM;
            case "forgot-password", "_forgot-password" -> ViewMap.Client.FORGOT_PASSWORD_FORM;
            case "reset-password", "_reset-password" -> ViewMap.Client.RESET_PASSWORD_FORM;
            case "product-detail", "_product-detail" -> ViewMap.Client.PRODUCT_DETAIL;
            default -> null;
        };
    }
}
