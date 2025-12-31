package com.laptrinhweb.zerostarcafe.web.client.servlet;

import com.laptrinhweb.zerostarcafe.core.utils.PathUtil;
import com.laptrinhweb.zerostarcafe.web.common.routing.AppRoute;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Handles dynamic modal loading requests from the client-side JavaScript.
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 27/12/2025
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

        String jspPath = PathUtil.Client.form(modalName);
        if (req.getServletContext().getResource(jspPath) == null) {
            AppRoute.sendError(HttpServletResponse.SC_NOT_FOUND,
                    "Modal not found, path=" + jspPath, resp);
            return;
        }

        req.getRequestDispatcher(jspPath).forward(req, resp);
    }
}

