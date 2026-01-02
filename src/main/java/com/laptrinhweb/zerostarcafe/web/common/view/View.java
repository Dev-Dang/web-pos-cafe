package com.laptrinhweb.zerostarcafe.web.common.view;

import com.laptrinhweb.zerostarcafe.web.common.utils.RequestUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
        String viewPath,
        Map<String, Object> metadata
) {
    public static final String AREA_KEY = "area";
    public static final String PAGE_TITLE = "pageTitle";
    public static final String PAGE_CONTENT = "pageContent";
    public static final String VIEW_META = "viewMeta";

    public View {
        metadata = metadata == null ? Map.of() : Map.copyOf(metadata);
    }

    /**
     * Create a full page view with title.
     */
    public static View page(ViewArea area, String viewPath, String titleKey) {
        return new View(area, titleKey, viewPath, Map.of());
    }

    /**
     * Create a partial view with builder for metadata.
     */
    public static PartialBuilder partial(ViewArea area, String viewPath) {
        return new PartialBuilder(area, viewPath);
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
    public static void render(View view, HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (view == null || req.getServletContext().getResource(view.viewPath()) == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Pass metadata to JSP
        if (!view.metadata.isEmpty()) {
            req.setAttribute(VIEW_META, view.metadata);
        }

        // Handle partial requests
        if (RequestUtils.isPartialRequest(req) || view.titleKey == null) {
            req.setAttribute(PAGE_CONTENT, view.viewPath);
            req.getRequestDispatcher(ViewArea.SHARED.getLayoutPath()).forward(req, resp);
            return;
        }

        // Handle full page rendering
        req.setAttribute(AREA_KEY, view.area());
        req.setAttribute(PAGE_TITLE, view.titleKey());
        req.setAttribute(PAGE_CONTENT, view.viewPath());

        req.getRequestDispatcher(view.area().getLayoutPath()).forward(req, resp);
    }

    /**
     * Builder for partial views with metadata.
     */
    public static class PartialBuilder {
        private final ViewArea area;
        private final String viewPath;
        private final Map<String, Object> metadata = new HashMap<>();

        private PartialBuilder(ViewArea area, String viewPath) {
            this.area = area;
            this.viewPath = viewPath;
        }

        public PartialBuilder withMeta(String key, Object value) {
            metadata.put(key, value);
            return this;
        }

        public View build() {
            return new View(area, null, viewPath, metadata);
        }
    }
}