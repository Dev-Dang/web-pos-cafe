package com.laptrinhweb.zerostarcafe.web.common.servlet;

import com.laptrinhweb.zerostarcafe.web.common.view.View;
import com.laptrinhweb.zerostarcafe.web.common.view.ViewMap;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Renders the generic error page for any container-dispatched error.
 *
 * @author Dang Van Trung
 * @version 1.0.1
 * @lastModified 02/01/2026
 * @since 1.0.0
 */
@WebServlet(name = "ErrorServlet", urlPatterns = "/error")
public class ErrorServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        View.render(ViewMap.Shared.ERROR, req, resp);
    }
}