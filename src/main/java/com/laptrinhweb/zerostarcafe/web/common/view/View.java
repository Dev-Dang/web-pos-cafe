package com.laptrinhweb.zerostarcafe.web.common.view;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Objects;

/**
 * <h2>Description:</h2>
 * <p>
 * Represents a resolved view, including its layout, page path, title key,
 * and UI area (client/admin). Instances are typically created through
 * {@link PageResolver} using semantic URLs rather than direct JSP paths.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * View view = View.client("/home");
 * View.render(view, req, resp);
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 09/12/2025
 * @since 1.0.0
 */
public record View(
        ViewArea area,
        Type type,

        String title,
        String viewPath,
        String layoutPath
) {
    public static final String AREA_KEY = "area";
    public static final String PAGE_TITLE = "pageTitle";
    public static final String PAGE_CONTENT = "pageContent";

    public enum Type {
        PAGE,
        COMPONENT;
    }

    public static View getPage(ViewArea area, String viewPath) {
        return PageResolver.resolve(area, viewPath);
    }

    public static View getComponent(ViewArea area, String viewPath) {
        return new View(area, Type.COMPONENT, null, viewPath, null);
    }

    public boolean isDefault() {
        View defaultView = ViewMap.getDefaultFor(this.area);
        return this.equals(defaultView);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        View view = (View) o;
        return type == view.type
                && Objects.equals(title, view.title)
                && area == view.area
                && Objects.equals(viewPath, view.viewPath)
                && Objects.equals(layoutPath, view.layoutPath);
    }

    /**
     * Renders the given view by forwarding the request to its layout JSP. </br>
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

        if (view == null || req.getServletContext().getResource(view.viewPath) == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        switch (view.type()) {
            case PAGE -> {
                req.setAttribute(AREA_KEY, view.area());
                req.setAttribute(PAGE_TITLE, view.title());
                req.setAttribute(PAGE_CONTENT, view.viewPath());

                req.getRequestDispatcher(view.layoutPath()).forward(req, resp);
            }
            case COMPONENT -> {
                req.getRequestDispatcher(view.viewPath()).forward(req, resp);
            }
            default -> throw new IllegalStateException("Unexpected value: " + view.type());
        }
    }
}