package com.laptrinhweb.zerostarcafe.web.common.servlet;

import com.laptrinhweb.zerostarcafe.web.common.routing.AppRoute;
import com.laptrinhweb.zerostarcafe.web.common.routing.RouteMap;
import com.laptrinhweb.zerostarcafe.web.common.utils.RequestUtils;
import com.laptrinhweb.zerostarcafe.web.common.view.View;
import com.laptrinhweb.zerostarcafe.web.common.view.ViewArea;
import com.laptrinhweb.zerostarcafe.web.common.view.ViewMap;
import com.laptrinhweb.zerostarcafe.web.common.view.ViewResolver;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Base servlet for all requests not handled by other servlets.
 * Static resources are forwarded to the container’s default servlet.
 * Dynamic paths are resolved to a {@link View} and rendered via {@link View#render}.
 *
 * @author Dang Van Trung
 * @version 2.0.0
 * @lastModified 11/12/2025
 * @since 1.0.0
 */
@WebServlet(name = "BaseServlet", urlPatterns = "/")
public class BaseServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String uri = req.getRequestURI();
        String ctx = req.getContextPath();
        String path = uri.substring(ctx.length());

        // ==== Ignore static file path ====
        if (RequestUtils.isStaticRequest(req)) {
            RequestDispatcher rd = getServletContext().getNamedDispatcher("default");
            rd.forward(req, resp);
            return;
        }

        // ==== View resolution and rendering ====
        ViewArea area = ViewArea.detectArea(path);
        View view = ViewResolver.resolve(area, path);

        // If view matches default for area, redirect to proper route
        View defaultView = ViewMap.getDefaultFor(area);
        if (view.equals(defaultView)) {
            switch (area) {
                case ADMIN -> AppRoute.redirect(RouteMap.DASHBOARD, req, resp);
                default -> AppRoute.redirect(RouteMap.HOME, req, resp);
            }
            return;
        }

        View.render(view, req, resp);
    }
}