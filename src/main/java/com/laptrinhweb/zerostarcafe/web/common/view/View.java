package com.laptrinhweb.zerostarcafe.web.common.view;

import com.laptrinhweb.zerostarcafe.web.common.routing.AppRoute;
import com.laptrinhweb.zerostarcafe.web.common.utils.RequestUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;

import java.io.IOException;

/**
 * <h2>Description:</h2>
 * <p>
 * Represents a view (JSP) with optional metadata.
 * All JSPs are views - either full pages or partials for Unpoly.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * View view = View.page(ViewArea.CLIENT, "/WEB-INF/views/client/pages/home.jsp", "general.client.home");
 * View.render(view, req, resp);
 *
 * // With metadata
 * View modal = View.partial(ViewArea.CLIENT, "/WEB-INF/views/client/forms/_login.jsp")
 *     .withMeta("modal", true)
 *     .build();
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 2.0.0
 * @lastModified 02/01/2026
 * @since 1.0.0
 */
public record View(
        ViewArea area,
        String titleKey,
        String viewPath
) {
    public static final String AREA_KEY = "area";
    public static final String PAGE_TITLE = "pageTitle";
    public static final String PAGE_CONTENT = "pageContent";

    /**
     * Create a full page view with title.
     */
    public static View page(ViewArea area, String viewPath, String titleKey) {
        return new View(area, titleKey, viewPath);
    }

    /**
     * Create a partial view without title.
     */
    public static View partial(ViewArea area, String viewPath) {
        return new View(area, null, viewPath);
    }

    /**
     * Renders the given view by forwarding the request to its layout JSP.
     * Sends a 404 response if the view is {@code null} or the page does not exist.
     *
     * @param view the resolved view to render
     * @param req  current HTTP request
     * @param resp current HTTP response
     * @throws ServletException if the forwarding fails
     * @throws IOException      if the response cannot be written
     */
    public static void render(@NonNull View view,
                              HttpServletRequest req,
                              HttpServletResponse resp)
            throws ServletException, IOException {

        // Handle invalid (not found) view paths
        if (req.getServletContext().getResource(view.viewPath()) == null) {
            AppRoute.sendError(HttpServletResponse.SC_NOT_FOUND, resp);
            return;
        }

        // Handle partial requests
        if (RequestUtils.isPartialRequest(req) || view.titleKey == null) {
            req.setAttribute(PAGE_CONTENT, view.viewPath);
            AppRoute.forward(ViewArea.SHARED.getLayoutPath(), req, resp);
            return;
        }

        // Handle full page rendering
        req.setAttribute(AREA_KEY, view.area());
        req.setAttribute(PAGE_TITLE, view.titleKey());
        req.setAttribute(PAGE_CONTENT, view.viewPath());

        AppRoute.forward(view.area().getLayoutPath(), req, resp);
    }
}