package com.laptrinhweb.zerostarcafe.web.auth.filter;

import com.laptrinhweb.zerostarcafe.core.security.SecurityKeys;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthUser;
import com.laptrinhweb.zerostarcafe.domain.user.model.UserRole;
import com.laptrinhweb.zerostarcafe.web.common.response.Message;
import com.laptrinhweb.zerostarcafe.web.common.routing.AppRoute;
import com.laptrinhweb.zerostarcafe.web.common.routing.RouteMap;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * <h2>Description:</h2>
 * <p>
 * This filter checks the user's role before allowing access to
 * protected areas such as "/admin", "/manager", or "/staff".
 * Also enforces authentication for client routes like "/payment", "/cart", "/order".
 * If the user is not logged in or does not have the correct role,
 * the filter blocks the request.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.1.0
 * @lastModified 09/01/2026
 * @since 1.0.0
 */
@WebFilter(filterName = "RoleFilter", urlPatterns = {
        "/admin/*",
        "/manager/*",
        "/staff/*",
        "/payment/*",
        "/cart/*",
        "/order/*",
        "/loyalty/*"
})
public class RoleFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        AuthUser user = (AuthUser) request.getSession(false)
                .getAttribute(SecurityKeys.SESSION_AUTH_USER);

        String ctx = request.getContextPath();       // /zero_star_cafe
        String uri = request.getRequestURI();        // /zero_star_cafe/admin/dashboard
        String path = uri.substring(ctx.length());   // /admin/dashboard

        // User not logged in -> redirect to home with appropriate message
        if (user == null) {
            // Different message for client vs staff routes
            if (isClientRoute(path)) {
                Message.warn(request, "general.client.requiredLogin");
            } else {
                Message.error(request, "general.error.userNotLoggedIn");
            }
            AppRoute.redirect(RouteMap.HOME, request, response);
            return;
        }

        // Check role requirements for staff/admin routes
        if (path.startsWith("/admin/") && !user.hasRole(UserRole.SUPER_ADMIN)) {
            AppRoute.sendError(HttpServletResponse.SC_FORBIDDEN, response);
            return;
        }

        if (path.startsWith("/manager/") && !user.hasRole(UserRole.STORE_MANAGER)) {
            AppRoute.sendError(HttpServletResponse.SC_FORBIDDEN, response);
            return;
        }

        if (path.startsWith("/staff/") && !user.hasRole(UserRole.STAFF)) {
            AppRoute.sendError(HttpServletResponse.SC_FORBIDDEN, response);
            return;
        }

        // Client routes (/payment, /cart, /order) only need authentication, no role check
        // Authentication was already verified above (user != null)

        // User passed all checks -> continue request
        chain.doFilter(req, resp);
    }

    /**
     * Check if path is a client route (not staff/admin)
     */
    private boolean isClientRoute(String path) {
        return path.startsWith("/payment/") 
            || path.startsWith("/cart/") 
            || path.startsWith("/order/")
            || path.startsWith("/loyalty/");
    }
}