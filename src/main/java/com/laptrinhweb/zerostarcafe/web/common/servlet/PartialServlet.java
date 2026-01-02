package com.laptrinhweb.zerostarcafe.web.common.servlet;

import com.laptrinhweb.zerostarcafe.web.common.utils.RequestUtils;
import com.laptrinhweb.zerostarcafe.web.common.view.View;
import com.laptrinhweb.zerostarcafe.web.common.view.ViewMap;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Single unified endpoint for fetching all partial views.
 * Uses ViewMap to resolve views by name - one place for all partials.
 *
 * <h2>Usage:</h2>
 * <pre>
 * GET /partial/login-form      → ViewMap.Client.LOGIN_FORM
 * GET /partial/register-form   → ViewMap.Client.REGISTER_FORM
 * GET /partial/cart-sidebar    → ViewMap.Client.CART_SIDEBAR
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 02/01/2026
 * @since 1.0.0
 */
@WebServlet(name = "PartialServlet", urlPatterns = "/partial/*")
public class PartialServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            // Extract partial name from path: /partial/login-form → login-form
            String partialName = RequestUtils.extractStringParam(req, "Partial name");

            // Let ViewMap resolve the view by name (centralized)
            View partial = ViewMap.getPartialByName(partialName);

            if (partial == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Partial not found: " + partialName);
                return;
            }

            View.render(partial, req, resp);

        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }
}
